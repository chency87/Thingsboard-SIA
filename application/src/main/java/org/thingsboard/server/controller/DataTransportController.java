package org.thingsboard.server.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.quartz.CronExpression;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.thingsboard.server.bean.MqttConfig;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.exception.ThingsboardException;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.kv.TsKvEntry;
import org.thingsboard.server.common.data.page.TextPageData;
import org.thingsboard.server.common.data.page.TextPageLink;
import org.thingsboard.server.controller.BaseController;
import org.thingsboard.server.controller.idscontroller.ConstantConfValue;
import org.thingsboard.server.dao.model.sql.MqttEntity;
import org.thingsboard.server.dao.timeseries.TimeseriesService;
import org.thingsboard.server.executejob.BaseDataTransportJob;
import org.thingsboard.server.service.ids.MqttTransportService;
import org.thingsboard.server.service.ids.QuartzManager;
import org.thingsboard.server.service.security.model.SecurityUser;
import org.thingsboard.server.utils.JsonConvertUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@RestController
@RequestMapping("/api")
public class DataTransportController extends BaseController {
    @Autowired
    private TimeseriesService tsService;

    @Autowired
    private QuartzManager quartzManager;

    @Autowired
    private MqttTransportService mqttTransportService;

//    public final static ConcurrentHashMap<String,String> runningTransportationMap = new ConcurrentHashMap<>();

    public final static ConcurrentHashMap<String,Map<String,Map<String,String>>> transportationParamsMap = new ConcurrentHashMap<>();


    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN')")
    @RequestMapping(value = "/plugin/data/transport/exec2", method = RequestMethod.POST)
    @ResponseBody
    public String postTransportParams222(@RequestBody String json) {
        return json;
    }


    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN')")
    @RequestMapping(value = "/plugin/data/transport/stop", method = RequestMethod.POST)
    @ResponseBody
    public void stopTransportParams(@RequestBody String json) throws ThingsboardException, SchedulerException {
        SecurityUser user = getCurrentUser();
        JsonConvertUtils.releaseTransportationParamsMap(user.getTenantId().toString(),json);
        if(DataTransportController.transportationParamsMap.size() == 0){
            String jobGroupName = user.getTenantId()+ ConstantConfValue.dataTransportJobGroupNameSuffix;
            quartzManager.deleteJob(ConstantConfValue.dataTransportJobName,jobGroupName);
        }
    }



    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN')")
    @RequestMapping(value = "/plugin/data/transport/exec", method = RequestMethod.POST)
    @ResponseBody
    public DeferredResult<ResponseEntity> postTransportParams(@RequestBody String json) throws ThingsboardException, SchedulerException {
        JsonObject object = new JsonParser().parse(json).getAsJsonObject();
        String cronVal = object.get("cron").getAsString();
        String cron = cronVal == null?  "0/1 * * * * ?" : cronVal;
        if(!isValidExpression(cron)){
            return getImmediateDeferredResult("Wrong cron expression", HttpStatus.BAD_REQUEST);
        }
        String param = object.get("param").toString();
        SecurityUser user = getCurrentUser();
        Map<String,Object> jobData = new HashMap<>();
        String jobGroupName = user.getTenantId()+ ConstantConfValue.dataTransportJobGroupNameSuffix;
        jobData.put("tenantId",user.getTenantId());
        JsonConvertUtils.updateTransportationParamsMap(user.getTenantId().toString(),param);
        if (hasJob(ConstantConfValue.dataTransportJobName)){
            quartzManager.deleteJob(ConstantConfValue.dataTransportJobName,jobGroupName);
        }
        quartzManager.addJob(BaseDataTransportJob.class,ConstantConfValue.dataTransportJobName,jobGroupName,cron,jobData);
        return getImmediateDeferredResult("SUCCESS", HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('TENANT_ADMIN')")
    @RequestMapping(value = "/plugin/data/all", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,List<Map<String,String>>> getAllData(@RequestParam(value = "limits", required = false, defaultValue = "20") Integer limit,
                                                     @RequestParam(required = false) String idOffset) throws ThingsboardException {
        List<Map<String,String>> devList = new ArrayList<>();
        Map<String,List<Map<String,String>>> retMap = new HashMap<>();
        String lastId = "";
        Boolean hasNext = false;

        try {
            TenantId tenantId = getCurrentUser().getTenantId();
            TextPageLink pageLink = createPageLink(limit, null, idOffset, null);
            TextPageData<Device> pageData =  deviceService.findDevicesByTenantId(tenantId, pageLink);
            hasNext = pageData.hasNext();
            List<Device> list =pageData.getData();
            for (Device dev: list) {
                List<TsKvEntry> tsKvEntryList = tsService.findAllLatest(tenantId, dev.getId()).get();
                lastId = dev.getId().toString();
                if(tsKvEntryList.size() > 0){
                    for (TsKvEntry entry:tsKvEntryList){
                        Map<String,String> ret = new HashMap<>();
                        ret.put("name",dev.getName());
                        ret.put("id",dev.getId().toString());
                        ret.put("scope","(*,*)");
                        ret.put("key",entry.getKey());
                        ret.put("LastValue",entry.getValue().toString());
                        ret.put("ts",String.valueOf(entry.getTs()));
                        ret.put("status",String.valueOf(existInRunningTransport(tenantId.toString(),dev.getId().toString(),entry.getKey())));
                        devList.add(ret);
                    }
                }else {
                    Map<String,String> ret = new HashMap<>();
                    ret.put("name",dev.getName());
                    ret.put("id",dev.getId().toString());
                    ret.put("status","false");
                    devList.add(ret);
                }
            }
        } catch (Exception e) {
            throw handleException(e);
        }

        List<Map<String,String>> pageLink = new ArrayList<>();
        Map<String,String> pageInfo = new HashMap<>();
        pageInfo.put("idOffset",lastId);
        pageInfo.put("preIdOffset",idOffset);
        pageInfo.put("hasNext",hasNext.toString());
        pageLink.add(pageInfo);
        retMap.put("data",devList);
        retMap.put("page",pageLink);
        return retMap;
    }


    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN')")
    @RequestMapping(value = "/config/transport/save", method = RequestMethod.POST)
    @ResponseBody
    public DeferredResult<ResponseEntity> configMqttSave(MqttEntity mqttConfig) throws Exception {

        boolean b = mqttTransportService.configMqttSave(mqttConfig);
        if (b){
            return getImmediateDeferredResult("SUCCESS", HttpStatus.OK);
        }
       return getImmediateDeferredResult("FAIL",HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN')")
    @RequestMapping(value = "/config/transport/get", method = RequestMethod.GET)
    @ResponseBody
    public List<MqttEntity> configMqttGet() throws ThingsboardException {
        SecurityUser user = getCurrentUser();
        TenantId tenantId = user.getTenantId();
        List<MqttEntity> mqttEntities = mqttTransportService.configMqttGet(tenantId.toString());
        return mqttEntities;
    }


/*    private static final String MQTT_URL = "tcp://localhost:1883";

    private static final String CLIENT_ID = "MQTT_TCP_JAVA_CLIENT";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "public";*/


    private DeferredResult<ResponseEntity> getImmediateDeferredResult(String message, HttpStatus status) {
        DeferredResult<ResponseEntity> result = new DeferredResult<>();
        result.setResult(new ResponseEntity<>(message, status));
        return result;
    }

    private boolean isValidExpression(String cronExpression){
        return cronExpression != null && CronExpression.isValidExpression(cronExpression);
    }
    private boolean existInRunningTransport(String tenantId, String entityId, String key){
        if(DataTransportController.transportationParamsMap.get(tenantId) == null)
            return false;
        if(DataTransportController.transportationParamsMap.get(tenantId).get(entityId) == null)
            return false;
        if(DataTransportController.transportationParamsMap.get(tenantId).get(entityId).get(key) ==null)
            return false;
        return true;
    }

    private Boolean hasJob(String jobName){
        List<Map<String, Object>> list = quartzManager.queryAllJob();
        for(Map<String, Object> map : list){
            String name = map.get("jobName").toString();
            if (jobName == name) return true;
        }
        return false;
    }

}

//        json =" [{\n" +
//                "            \"id\": {\n" +
//                "                \"entityType\": \"DEVICE\",\n" +
//                "                        \"id\": \"d2562ff0-bb46-11ea-ac7d-337c2584aace\"\n" +
//                "            },\n" +
//                "            \"keys\": [{\n" +
//                "                \"key\": \"tmp\",\n" +
//                "                        \"scope\": \"(*,*)\"\n" +
//                "            }, {\n" +
//                "                \"key\": \"humity\",\n" +
//                "                        \"scope\": \"(*,*)\"\n" +
//                "            }]\n" +
//                "        }, {\n" +
//                "            \"id\": {\n" +
//                "                \"entityType\": \"DEVICE\",\n" +
//                "                        \"id\": \"85cacf50-bb6a-11ea-8cc4-73c0690c1ed2\"\n" +
//                "            },\n" +
//                "            \"keys\": [{\n" +
//                "                \"key\": \"key1\",\n" +
//                "                        \"scope\": \"(*,*)\"\n" +
//                "            }, {\n" +
//                "                \"key\": \"key2\",\n" +
//                "                        \"scope\": \"(*,*)\"\n" +
//                "            }]\n" +
//                "        }]";