package org.thingsboard.server.controller.idscontroller;

import com.google.common.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import org.thingsboard.server.common.data.exception.ThingsboardException;
import org.thingsboard.server.common.data.id.*;
import org.thingsboard.server.common.data.kv.*;
import org.thingsboard.server.controller.BaseController;
import org.thingsboard.server.dao.timeseries.TimeseriesService;
import org.thingsboard.server.plugin.DataFetchPluginManager;
import org.thingsboard.server.plugin.bean.DataFetchPlugin;
import org.thingsboard.server.service.plugin.AsyncService;
import org.thingsboard.server.service.security.AccessValidator;
import org.thingsboard.server.service.security.model.SecurityUser;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
public class PluginController  extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PluginController.class);

    @Autowired
    private AsyncService asyncPluginService;

    @Autowired
    private DataFetchPluginManager dfp;


    @Value("${transport.json.max_string_value_length:0}")
    private int maxStringValueLength;

    private ExecutorService executor;

    @PostConstruct
    public void initExecutor() {
        executor = Executors.newSingleThreadExecutor();
    }

    @PreDestroy
    public void shutdownExecutor() {
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/{entityType}/{entityId}/plugin/do", method = RequestMethod.GET)
    @ResponseBody
    public String doAcquire( @PathVariable("entityType") String entityType, @PathVariable("entityId") String entityIdStr,
                             @RequestParam(name = "status") Boolean status ) throws ThingsboardException, ExecutionException, InterruptedException {

        String pluginName = null;
        String keysStr = getKeys();
        SecurityUser user = getCurrentUser();
        List<String> keyList = toKeysList(keysStr);
        ListenableFuture<List<AttributeKvEntry>> listListenableFuture = attributesService.find(user.getTenantId(), EntityIdFactory.getByTypeAndId(entityType, entityIdStr), null, keyList);
        List<AttributeKvEntry> attributeKvEntries = listListenableFuture.get();
        for (AttributeKvEntry attributeKvEntry:attributeKvEntries) {
            Optional<String> strValue = attributeKvEntry.getStrValue();
            pluginName = strValue.get();
        }

        if (!StringUtils.isEmpty(pluginName)) {
            DataFetchPlugin plugin = dfp.getPlginInfoFromList(pluginName);
            plugin.setStatus(status);
            Map<String,String> map = new HashMap<>();
            for(int i =0;i<plugin.getRequires().size();i++){
                map.put(plugin.getRequires().get(i),"Attr");
            }
            log.info("Start submit Data Fetch task to pool");
            asyncPluginService.executeAsyncService(plugin,map);
            return "success";
        }else {
            return "plugin not exist";
        }


        //attributesService.find()

        //Step 1 根据实体ID获取其全部属性信息
        //Step 2 从设备属性中获取设备所采用的插件名称
        //Step 3 若设备还未设置采用的数采插件或者设备采用的插件已经被停用，则返回错误信息
        //Step 4 若设备已经设置采用的数采插件，则根据该插件名称获取其需要的全部参数信息，并从设备属性信息中查询，对插件需要的参数进行一一赋值
        //Step 5 将插件信息以及其所需要的配置信息做为参数，调用异步执行算法
        //若不为空，则执行下述异步方法

    }


    //TODO: 上传或更新协议插件
    @RequestMapping(value = "/plugin/put", method = RequestMethod.POST )
    public Boolean putXML(@RequestBody DataFetchPlugin dataFetchPlugin) {
        return dfp.addProto(dataFetchPlugin);
    }

    //TODO: 删除插件
    @RequestMapping(value = "/plugin/delete", method = RequestMethod.POST )
    public Boolean deleteXML(@RequestBody DataFetchPlugin dataFetchPlugin) {
        return dfp.deleteProto(dataFetchPlugin);
    }


    //TODO: 上传文件
    @PostMapping("/plugin/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }

        String fileName = file.getOriginalFilename();
        String filePath = System.getProperty("user.dir")+"/target/conf/";
        File dest = new File(filePath + fileName);
        try {
            file.transferTo(dest);
            LOGGER.info("上传成功");
            return "上传成功";
        } catch (IOException e) {
            LOGGER.error(e.toString(), e);
        }
        return "上传失败！";
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


}
