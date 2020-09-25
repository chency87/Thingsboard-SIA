package org.thingsboard.server.controller.idscontroller;

import com.google.common.util.concurrent.ListenableFuture;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import org.thingsboard.server.common.data.DataConstants;
import org.thingsboard.server.common.data.exception.ThingsboardException;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.data.id.EntityIdFactory;
import org.thingsboard.server.common.data.kv.AttributeKvEntry;
import org.thingsboard.server.common.data.kv.BaseAttributeKvEntry;
import org.thingsboard.server.common.data.kv.StringDataEntry;
import org.thingsboard.server.controller.BaseController;
import org.thingsboard.server.plugin.DataFetchProtoHandlePluginManager;
import org.thingsboard.server.plugin.bean.DataFetchPlugin;
import org.thingsboard.server.service.security.model.SecurityUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@RestController
@RequestMapping("/api")
public class ProtoHandlePluginController  extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProtoHandlePluginController.class);

    @Autowired
    private DataFetchProtoHandlePluginManager dfp;

    @ApiOperation(value = "查看所有系统内添加的插件")
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN')")
    @RequestMapping(value = "/proto/handle/plugin/all", method = RequestMethod.GET )
    public List<DataFetchPlugin> getAllPlugin(){
        return dfp.getPluginList();
    }


    @ApiOperation(value = "上传协议插件",notes = "参数plugin为需要上传的插件协议jar包；参数name为插件名称；" +
            "参数status为true表示协议可用；参数requires为使用此插件需要上传的配置信息")
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN')")
    @RequestMapping(value = "proto/handle/plugin/upload", method = RequestMethod.POST )
    public DeferredResult<ResponseEntity> uploadFileAndData(@RequestParam(value = "plugin") MultipartFile file, @RequestParam(value = "name") String name,
                                                            @RequestParam(value = "classname") String className,@RequestParam(value = "status",defaultValue = "true") boolean status,
                                                            @RequestParam(value = "requires") String requires) throws FileNotFoundException {
        File folder = new File(System.getProperty("user.dir"), ConstantConfValue.xmlPluginFolder);
        if(dfp.hasPlugin(name))
        {
            return getImmediateDeferredResult("There is already an plugin named :" + name,HttpStatus.OK);
        }
        // 对上传的文件重命名，避免文件重名
        try {
            // 文件保存
            file.transferTo(new File(folder, name+".jar"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        DataFetchPlugin dataFetchPlugin = new DataFetchPlugin();
        dataFetchPlugin.setClassName(className);
        dataFetchPlugin.setName(name);
        dataFetchPlugin.setJar(new File(folder, name+".jar").getAbsolutePath());
        dataFetchPlugin.setStatus(status);

        List<String> requiresList = Arrays.asList(requires.split(","));

        dataFetchPlugin.setRequires(requiresList);
        dfp.addProto(dataFetchPlugin);
        return getImmediateDeferredResult("Success",HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN')")
    @RequestMapping(value = "/proto/handle/plugin/del", method = RequestMethod.DELETE )
    public boolean delPlugin( @RequestParam(value = "name") String name) throws MalformedURLException, DocumentException {

        return dfp.deleteProto(name);
    }

    @ApiOperation(value = "获取所有插件的名称列表")
    @PreAuthorize("hasAnyAuthority( 'TENANT_ADMIN')")
    @RequestMapping(value = "/proto/handle/plugin/get", method = RequestMethod.GET )
    public List<String> getAllPluginName() throws MalformedURLException, DocumentException {
        List<String> pluginNames = new ArrayList<>();
        List<DataFetchPlugin> dataFetchPlugins = dfp.updatePluginList();
        if(dataFetchPlugins !=null){
            dataFetchPlugins.forEach(dataFetchPlugin -> {pluginNames.add(dataFetchPlugin.getName());});
        }
        return pluginNames;
    }

    /**
     * 显示选中协议的配置信息，若有配置，显示配置信息，若没有配置过，则只显示需要配置的键，值为空。
     * @param entityType
     * @param entityIdStr
     * @return map
     * @throws ThingsboardException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @ApiOperation(value = "显示设备的配置信息",notes = "若有配置，显示配置信息，若没有配置过，则只显示需要配置的键，值为空")
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/{entityType}/{entityId}/device/config", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> show( @PathVariable("entityType") String entityType, @PathVariable("entityId") String entityIdStr,
                                    @RequestParam (required=false)  String protocol
    ) throws ThingsboardException, ExecutionException, InterruptedException, MalformedURLException, DocumentException {
        Map<String, Object> map = new HashMap<>();
        SecurityUser user = getCurrentUser();
        EntityId entity = EntityIdFactory.getByTypeAndId(entityType, entityIdStr);
        //判断前端是否传入协议，若没传入则使用此设备配置好的协议信息
        if ( org.apache.commons.lang3.StringUtils.isEmpty(protocol)
                || org.apache.commons.lang3.StringUtils.isBlank(protocol)){
            List<String> keyList = toKeysList(ConstantConfValue.dataFetchPluginName);
            ListenableFuture<List<AttributeKvEntry>> listListenableFuture = attributesService.find(user.getTenantId(), entity, DataConstants.IDENTITY_SCOPE, keyList);
            List<AttributeKvEntry> attributeKvEntries = listListenableFuture.get();
            //若既没有传入协议，此设备也没有配置，直接返回空值
            if (attributeKvEntries == null){
                return map;
            }
            attributeKvEntries.forEach(attributeKvEntry -> {
                String pluginName = attributeKvEntry.getValueAsString();
                try {
                    List<String> requiresList = getRequiresList(pluginName);
                    ListenableFuture<List<AttributeKvEntry>> future = attributesService.find(user.getTenantId(), entity, DataConstants.IDENTITY_SCOPE, requiresList);
                    try {
                        future.get().forEach(attributeKv -> { map.put(attributeKv.getKey(), attributeKv.getValue()); });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            //传入协议了
        }else{
            List<String> keyList = toKeysList(protocol);
            DataFetchPlugin plugin = dfp.getPluginInfoFromList(protocol);
            List<String> requires =  plugin.getRequires();
            ListenableFuture<List<AttributeKvEntry>> listListenableFuture1 = attributesService.find(user.getTenantId(), entity, DataConstants.IDENTITY_SCOPE, requires);
            if(listListenableFuture1.get().size() > 0){
                try {
                    listListenableFuture1.get().forEach(attributeKvEntry1 -> { map.put(attributeKvEntry1.getKey(), attributeKvEntry1.getValue());  });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }else {
                requires.forEach(item -> map.put(item,""));
            }
        }
        return map;
    }


    private List<String> getRequiresList(@RequestParam(name = "protocol") String protocol) throws DocumentException, MalformedURLException {
        List<String> list = new ArrayList<>();
        List<DataFetchPlugin> dataFetchPlugins = dfp.updatePluginList();
        for (DataFetchPlugin dataFetchPlugin : dataFetchPlugins){
            if (dataFetchPlugin.getName().equals(protocol)) {
                list = dataFetchPlugin.getRequires();
                //list.set(dataFetchPlugin.getRequires());
            }
        }
        return list;
    }

    /**
     *  保存设备协议信息
     * @param entityType
     * @param entityIdStr
     * @param map       需要写入向数据库的属性
     * @return
     */
    @ApiOperation(value = "将配置的设备协议信息进行保存/更新")
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/{entityType}/{entityId}/device/save", method = RequestMethod.POST)
    @ResponseBody
    public DeferredResult<ResponseEntity> saveConfigInfo(@PathVariable("entityType") String entityType, @PathVariable("entityId") String entityIdStr,
                                                         @RequestBody Map<String,String> map,
                                                         @RequestParam(name = "protocol") String protocol) throws ThingsboardException {
        List<AttributeKvEntry> list = new ArrayList<>();
        if (map.isEmpty()) return getImmediateDeferredResult("FAILURE",HttpStatus.NO_CONTENT);
        map.put(ConstantConfValue.dataFetchPluginName,protocol);
        SecurityUser user = getCurrentUser();
        EntityId entity = EntityIdFactory.getByTypeAndId(entityType, entityIdStr);
        map.entrySet().forEach(s -> {
            list.add(new BaseAttributeKvEntry(new StringDataEntry(s.getKey(), s.getValue()), System.currentTimeMillis()));
        });
        ListenableFuture<List<Void>> result = attributesService.save(user.getTenantId(),entity,DataConstants.IDENTITY_SCOPE,list);
        return getImmediateDeferredResult("INSERT SUCCESS", HttpStatus.OK);
    }


    private DeferredResult<ResponseEntity> getImmediateDeferredResult(String message, HttpStatus status) {
        DeferredResult<ResponseEntity> result = new DeferredResult<>();
        result.setResult(new ResponseEntity<>(message, status));
        return result;
    }

    private List<String> toKeysList(String keys) {
        List<String> keyList = null;
        if (!StringUtils.isEmpty(keys)) {
            keyList = Arrays.asList(keys.split(","));
        }
        return keyList;
    }
}
