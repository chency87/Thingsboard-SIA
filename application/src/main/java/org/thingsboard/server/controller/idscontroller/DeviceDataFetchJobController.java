package org.thingsboard.server.controller.idscontroller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.swagger.annotations.ApiOperation;
import oracle.jdbc.internal.ObjectData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.thingsboard.rule.engine.api.msg.DeviceAttributesEventNotificationMsg;
import org.thingsboard.server.common.data.BaseData;
import org.thingsboard.server.common.data.DataConstants;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.exception.ThingsboardException;
import org.thingsboard.server.common.data.id.DeviceId;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.data.id.EntityIdFactory;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.kv.AttributeKvEntry;
import org.thingsboard.server.common.data.kv.BaseAttributeKvEntry;
import org.thingsboard.server.common.data.kv.KvEntry;
import org.thingsboard.server.common.data.kv.StringDataEntry;
import org.thingsboard.server.common.data.page.TextPageData;
import org.thingsboard.server.common.data.page.TextPageLink;
import org.thingsboard.server.common.data.security.DeviceCredentials;
import org.thingsboard.server.common.msg.cluster.SendToClusterMsg;
import org.thingsboard.server.controller.BaseController;
import org.thingsboard.server.executejob.BaseDataFetchJob;
import org.thingsboard.server.plugin.DataFetchProtoHandlePluginManager;
import org.thingsboard.server.plugin.bean.DataFetchPlugin;
import org.thingsboard.server.service.ids.QuartzManager;
import org.thingsboard.server.service.security.model.SecurityUser;
import org.thingsboard.server.utils.JsonConvertUtils;

import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class DeviceDataFetchJobController extends BaseController {

    @Autowired
    private QuartzManager quartzManager;

    @Autowired
    private DataFetchProtoHandlePluginManager dfp;


    /**
     *  启动所有注册设备
     * @throws ThingsboardException
     */
    @ApiOperation(value = "启动所有注册设备",notes = "启动所有配置好的设备")
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value="/exec/all", method = RequestMethod.POST)
    public void startAllQuartzJob() throws ThingsboardException {

        quartzManager.execAllJob("DEVICE"+getCurrentUser().getEmail()+ConstantConfValue.dataFetchJobGroupNameSuffix);
//        TenantId tenantId = getCurrentUser().getTenantId();
//        TextPageLink pageLink = new TextPageLink(10000, null, null, null);
//        TextPageData<Device> deviceList= deviceService.findDevicesByTenantId(tenantId, pageLink);
//            if(hasJob(dev.getId().toString())){
//            }
//        }
    }

    /**
     *  暂停同一jobGroupName下的所有设备
     * @throws ThingsboardException
     */
    @ApiOperation(value = "停止所有设备",notes = "停止所有配置好的设备")
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value="/stop/all", method = RequestMethod.POST)
    public DeferredResult<ResponseEntity> stopAllQuartzJob(@PathVariable("entityType") String entityType) throws ThingsboardException {

        SecurityUser user = getCurrentUser();
        String jobGroupName = entityType + user.getEmail()+ ConstantConfValue.dataFetchJobGroupNameSuffix;
        quartzManager.stopAllJob(jobGroupName);
        return getImmediateDeferredResult("Stop Data Fetch Right", HttpStatus.OK);
    }

    /**
     *  启动或停止设备
     * @param entityType
     * @param entityIdStr
     * @param status            true/启动设备       false/停止
     * @param cron              设备定时采集时间间隔
     * @return
     * @throws ThingsboardException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @ApiOperation(value = "启动或者停止设备",notes = "输入参数status为true表示设备启动，false为停止；cron为采集数据周期，默认为1秒")
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/{entityType}/{entityId}/plugin/exec", method = RequestMethod.POST, params = {"status","cron"})
    @ResponseBody
    public DeferredResult<ResponseEntity> doAcquire( @PathVariable("entityType") String entityType, @PathVariable("entityId") String entityIdStr,
                             @RequestParam(name = "status") Boolean status,
                             @RequestParam(name = "cron",defaultValue = "0/1 * * * * ?") String cron) throws ThingsboardException, ExecutionException, InterruptedException {


        String pluginName = null;
        Boolean currentStatus = false;
        SecurityUser user = getCurrentUser();

        List<String> keyList = toKeysList(ConstantConfValue.dataFetchPluginName +",status");
        EntityId entity = EntityIdFactory.getByTypeAndId(entityType, entityIdStr);
        String jobName =entity.toString();

        ListenableFuture<List<AttributeKvEntry>> listListenableFuture = attributesService.find(user.getTenantId(), entity, DataConstants.IDENTITY_SCOPE, keyList);
        List<AttributeKvEntry> attributeKvEntries = listListenableFuture.get();
        for (AttributeKvEntry attributeKvEntry:attributeKvEntries) {
            if(attributeKvEntry.getKey().equals(ConstantConfValue.dataFetchPluginName)){
                pluginName = attributeKvEntry.getValueAsString();
            }else if(attributeKvEntry.getKey().equals("status")){
                currentStatus = attributeKvEntry.getBooleanValue().get();
            }
        }
        if (!StringUtils.isEmpty(pluginName)) {
            Map<String,String> jobData = new HashMap<>();
            DataFetchPlugin plugin = dfp.getPluginInfoFromList(pluginName);
            ArrayList<String> requires = (ArrayList<String>) plugin.getRequires();
            ListenableFuture<List<AttributeKvEntry>> RequireListenableFuture = attributesService.find(user.getTenantId(), entity, DataConstants.IDENTITY_SCOPE, requires);
            attributeKvEntries = RequireListenableFuture.get();
            for (AttributeKvEntry attributeKvEntry:attributeKvEntries) {
                String key = attributeKvEntry.getKey();
                jobData.put(key,attributeKvEntry.getValue().toString());
            }
            DeviceId deviceId = new DeviceId(toUUID(entityIdStr));
            DeviceCredentials deviceCredentialsByDeviceId = deviceCredentialsService.findDeviceCredentialsByDeviceId(getCurrentUser().getTenantId(), deviceId);
            String token = deviceCredentialsByDeviceId.getCredentialsId();
            String jobGroupName = entityType + user.getEmail()+ ConstantConfValue.dataFetchJobGroupNameSuffix;
            jobData.put(ConstantConfValue.dataFetchJobDataParamClassName,plugin.getClassName());
            jobData.put(ConstantConfValue.dataFetchJobDataParamJarPath,plugin.getJar());
            jobData.put(ConstantConfValue.dataFetchJobDataParamToken,token);
            if (status){
                if (hasJob(jobName)){
                    quartzManager.deleteJob(jobName,jobGroupName);
                }
                quartzManager.addJob(BaseDataFetchJob.class,jobName,jobGroupName,cron,jobData);
                currentStatus = true;
            }else {
                quartzManager.deleteJob(jobName,jobGroupName);
                currentStatus = false;
            }
            List<AttributeKvEntry> list = new ArrayList<>();
            list.add(new BaseAttributeKvEntry(new StringDataEntry("status", currentStatus.toString()), System.currentTimeMillis()));
            attributesService.save(user.getTenantId(),deviceId,DataConstants.IDENTITY_SCOPE,list);
            return getImmediateDeferredResult("SUCCESS", HttpStatus.OK);
        }else {
            return getImmediateDeferredResult("Execute Data Fetch Error", HttpStatus.BAD_REQUEST);
        }
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

    private Boolean hasJob(String jobName){
        List<Map<String, Object>> list = quartzManager.queryAllJob();
        for(Map<String, Object> map : list){
            String name = map.get("jobName").toString();
            if (jobName == name) return true;
        }
        return false;
    }

}
