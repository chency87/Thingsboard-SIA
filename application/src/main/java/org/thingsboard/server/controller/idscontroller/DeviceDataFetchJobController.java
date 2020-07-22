package org.thingsboard.server.controller.idscontroller;

import com.google.common.util.concurrent.ListenableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.exception.ThingsboardException;
import org.thingsboard.server.common.data.id.DeviceId;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.data.id.EntityIdFactory;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.kv.AttributeKvEntry;
import org.thingsboard.server.common.data.page.TextPageData;
import org.thingsboard.server.common.data.page.TextPageLink;
import org.thingsboard.server.common.data.security.DeviceCredentials;
import org.thingsboard.server.controller.BaseController;
import org.thingsboard.server.executejob.BaseDataFetchJob;
import org.thingsboard.server.plugin.DataFetchProtoHandlePluginManager;
import org.thingsboard.server.plugin.bean.DataFetchPlugin;
import org.thingsboard.server.service.ids.QuartzManager;
import org.thingsboard.server.service.security.model.SecurityUser;

import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class DeviceDataFetchJobController extends BaseController {

    @Autowired
    private QuartzManager quartzManager;

    @Autowired
    private DataFetchProtoHandlePluginManager dfp;


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

    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value="/stop/all", method = RequestMethod.POST)
    public void stopAllQuartzJob() throws ThingsboardException {
        quartzManager.stopAllJob("DEVICE"+getCurrentUser().getEmail()+ConstantConfValue.dataFetchJobGroupNameSuffix);
    }

    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/{entityType}/{entityId}/plugin/exec", method = RequestMethod.POST, params = {"status","cron"})
    @ResponseBody
    public DeferredResult<ResponseEntity> doAcquire( @PathVariable("entityType") String entityType, @PathVariable("entityId") String entityIdStr,
                             @RequestParam(name = "status") Boolean status,
                             @RequestParam(name = "cron",defaultValue = "0/1 * * * * ?") String cron) throws ThingsboardException, ExecutionException, InterruptedException {

        String pluginName = null;
        SecurityUser user = getCurrentUser();

        List<String> keyList = toKeysList(ConstantConfValue.dataFetchPluginName);
        EntityId entity = EntityIdFactory.getByTypeAndId(entityType, entityIdStr);

        ListenableFuture<List<AttributeKvEntry>> listListenableFuture = attributesService.find(user.getTenantId(), entity, "CLIENT_SCOPE", keyList);
        List<AttributeKvEntry> attributeKvEntries = listListenableFuture.get();
        for (AttributeKvEntry attributeKvEntry:attributeKvEntries) {
                Optional<String> strValue = attributeKvEntry.getStrValue();
                pluginName = strValue.get();
        }
//        pluginName

        if (!StringUtils.isEmpty(pluginName)) {
            Map<String,String> jobData = new HashMap<>();
            DataFetchPlugin plugin = dfp.getPluginInfoFromList(pluginName);
            ArrayList<String> requires = (ArrayList<String>) plugin.getRequires();
//            String req = "port,ip,funCode,slaveId,address,quantity,attr";
            ListenableFuture<List<AttributeKvEntry>> RequireListenableFuture = attributesService.find(user.getTenantId(), entity, "CLIENT_SCOPE", requires);
            attributeKvEntries = RequireListenableFuture.get();
            for (AttributeKvEntry attributeKvEntry:attributeKvEntries) {
                String key = attributeKvEntry.getKey();
                jobData.put(key,attributeKvEntry.getValue().toString());
            }

            DeviceId deviceId = new DeviceId(toUUID(entityIdStr));
            DeviceCredentials deviceCredentialsByDeviceId = deviceCredentialsService.findDeviceCredentialsByDeviceId(getCurrentUser().getTenantId(), deviceId);
            String token = deviceCredentialsByDeviceId.getCredentialsId();
            String jobName =entity.toString();
            String jobGroupName = entityType + user.getEmail()+ ConstantConfValue.dataFetchJobGroupNameSuffix;
            jobData.put(ConstantConfValue.dataFetchJobDataParamClassName,plugin.getClassName());
            jobData.put(ConstantConfValue.dataFetchJobDataParamJarPath,plugin.getJar());
            jobData.put(ConstantConfValue.dataFetchJobDataParamToken,token);

            if (status){
                if (hasJob(jobName)){
                    quartzManager.deleteJob(jobName,jobGroupName);
                }
                quartzManager.addJob(BaseDataFetchJob.class,jobName,jobGroupName,cron,jobData);
            }else {
                quartzManager.pauseJob(jobName,jobGroupName);
            }
            return getImmediateDeferredResult("SUCCESS", HttpStatus.OK);
        }else {
            return getImmediateDeferredResult("Execute Data Fetch Error", HttpStatus.OK);
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
