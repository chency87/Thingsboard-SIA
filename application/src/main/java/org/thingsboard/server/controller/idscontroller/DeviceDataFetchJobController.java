package org.thingsboard.server.controller.idscontroller;

import com.google.common.util.concurrent.ListenableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.thingsboard.server.common.data.exception.ThingsboardException;
import org.thingsboard.server.common.data.id.DeviceId;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.data.id.EntityIdFactory;
import org.thingsboard.server.common.data.kv.AttributeKvEntry;
import org.thingsboard.server.common.data.security.DeviceCredentials;
import org.thingsboard.server.controller.BaseController;
import org.thingsboard.server.controller.TbUrlConstants;
import org.thingsboard.server.executejob.BaseDataFetchJob;
import org.thingsboard.server.plugin.DataFetchPluginManager;
import org.thingsboard.server.plugin.bean.DataFetchPlugin;
import org.thingsboard.server.service.ids.QuartzManager;
import org.thingsboard.server.service.security.model.SecurityUser;
import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(TbUrlConstants.TELEMETRY_URL_PREFIX)
public class DeviceDataFetchJobController  extends BaseController {

    @Autowired
    private QuartzManager quartzManager;

    @Autowired
    private DataFetchPluginManager dfp;


    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping("/start/all")
    public void startAllQuartzJob() {
        quartzManager.startScheduler();
    }

    //
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/{entityType}/{entityId}/plugin/exec", method = RequestMethod.GET, params = {"status","cron","strDeviceId"})
    @ResponseBody
    public String doAcquire( @PathVariable("entityType") String entityType, @PathVariable("entityId") String entityIdStr,
                             @RequestParam(name = "status") Boolean status,
                             @RequestParam(name = "cron") String cron,
                             @RequestParam(name = "strDeviceId") String strDeviceId) throws ThingsboardException, ExecutionException, InterruptedException {

        // jobName = entityId
        // jobGroupName = entityType + "DATAFETCH"
        // jobTime : 可以定义一个默认值，然后前端也可以传递
        //封装到map
//        int jobTime = 5;
//        int jobTimes = 3;
        if (cron.isEmpty()) cron= "0/10 * * * * ?";

        String pluginName = null;
//        String keysStr = "pluginName,port,quantity,slaveId,ip,funCode,address,attr";
        String keysStr = getKeys();
        SecurityUser user = getCurrentUser();
        List<String> keyList = toKeysList(keysStr);
        EntityId entity = EntityIdFactory.getByTypeAndId(entityType, entityIdStr);

        ListenableFuture<List<AttributeKvEntry>> listListenableFuture = attributesService.find(user.getTenantId(), entity, "CLIENT_SCOPE", keyList);
        List<AttributeKvEntry> attributeKvEntries = listListenableFuture.get();
        for (AttributeKvEntry attributeKvEntry:attributeKvEntries) {
                Optional<String> strValue = attributeKvEntry.getStrValue();
                pluginName = strValue.get();
//                System.out.println("-------pluginName----------" + pluginName);
        }

        if (!StringUtils.isEmpty(pluginName)) {
            Map<String,String> jobData = new HashMap<>();
            DataFetchPlugin plugin = dfp.getPlginInfoFromList(pluginName);
            ArrayList<String> requires = (ArrayList<String>) plugin.getRequires();
//            String req = "port,ip,funCode,slaveId,address,quantity,attr";
            ListenableFuture<List<AttributeKvEntry>> RequireListenableFuture = attributesService.find(user.getTenantId(), entity, "CLIENT_SCOPE", requires);
            attributeKvEntries = RequireListenableFuture.get();
            for (AttributeKvEntry attributeKvEntry:attributeKvEntries) {
                String key = attributeKvEntry.getKey();
                jobData.put(key,attributeKvEntry.getValue().toString());
            }

            DeviceId deviceId = new DeviceId(toUUID(strDeviceId));
            DeviceCredentials deviceCredentialsByDeviceId = deviceCredentialsService.findDeviceCredentialsByDeviceId(getCurrentUser().getTenantId(), deviceId);
            String token = deviceCredentialsByDeviceId.getCredentialsId();
            String jobName =entity.toString();
            String jobGroupName = entityType + "DATAFETCH";
            jobData.put("className",plugin.getClassName());
            jobData.put("jarPath",plugin.getJar());
            jobData.put("token",token);

            if (status){
                if (hasJob(jobName)){
                    quartzManager.resumeJob(jobName,jobGroupName);
                }else {
                    quartzManager.addJob(BaseDataFetchJob.class,jobName,jobGroupName,cron,jobData);
                }
            }else {
                quartzManager.pauseJob(jobName,jobGroupName);
            }
            return "success";
        }else {
            return "error";
        }
//        quartzManager.deleteJob("job", "test");
//        quartzManager.addJob(BaseDataFetchJob.class, "job", "test", "0 * * * * ?", jobData);
//
//        jobData.put("name","2");
//        quartzManager.deleteJob("job2", "test");
//        quartzManager.addJob(BaseDataFetchJob.class, "job2", "test", "10 * * * * ?", jobData);
//
//        jobData.put("name","3");
//        quartzManager.deleteJob("job3", "test2");
//        quartzManager.addJob(BaseDataFetchJob.class, "job3", "test2", "15 * * * * ?", jobData);

    }



    private List<String> toKeysList(String keys) {
        List<String> keyList = null;
        if (!StringUtils.isEmpty(keys)) {
            keyList = Arrays.asList(keys.split(","));
        }
        return keyList;
    }

    private DeferredResult<ResponseEntity> getImmediateDeferredResult(String message, HttpStatus status) {
        DeferredResult<ResponseEntity> result = new DeferredResult<>();
        result.setResult(new ResponseEntity<>(message, status));
        return result;
    }

    private String getKeys(){

        return "pluginName";
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
