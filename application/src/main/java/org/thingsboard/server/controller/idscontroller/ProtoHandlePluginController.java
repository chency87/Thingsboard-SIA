package org.thingsboard.server.controller.idscontroller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import org.thingsboard.server.controller.BaseController;
import org.thingsboard.server.plugin.DataFetchProtoHandlePluginManager;
import org.thingsboard.server.plugin.bean.DataFetchPlugin;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class ProtoHandlePluginController  extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProtoHandlePluginController.class);

    @Autowired
    private DataFetchProtoHandlePluginManager dfp;

    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN')")
    @RequestMapping(value = "/proto/handle/plugin/all", method = RequestMethod.GET )
    public List<DataFetchPlugin> getAllPlugin(){
        return dfp.getPluginList();
    }


    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN')")
    @RequestMapping(value = "proto/handle/plugin/upload", method = RequestMethod.POST )
    public DeferredResult<ResponseEntity> uploadFileAndData(@RequestParam(value = "plugin") MultipartFile file, @RequestParam(value = "name") String name,
                                     @RequestParam(value = "classname") String className,@RequestParam(value = "status",defaultValue = "true") boolean status,
                                     @RequestParam(value = "requires") String requires) throws FileNotFoundException {
        File folder = new File(System.getProperty("user.dir"), ConstantConfValue.protoHandlePluginUpload);
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
    @RequestMapping(value = "/proto/handle/plugin/del", method = RequestMethod.GET )
    public boolean delPlugin(){
        return true;
    }

//
//
//    //TODO: 上传或更新协议插件
//    @RequestMapping(value = "/plugin/put", method = RequestMethod.POST )
//    public Boolean putXML(@RequestBody DataFetchPlugin dataFetchPlugin) {
//        return dfp.addProto(dataFetchPlugin);
//    }
//
//    //TODO: 删除插件
//    @RequestMapping(value = "/plugin/delete", method = RequestMethod.POST )
//    public Boolean deleteXML(@RequestBody DataFetchPlugin dataFetchPlugin) {
//        return dfp.deleteProto(dataFetchPlugin);
//    }
//


    private DeferredResult<ResponseEntity> getImmediateDeferredResult(String message, HttpStatus status) {
        DeferredResult<ResponseEntity> result = new DeferredResult<>();
        result.setResult(new ResponseEntity<>(message, status));
        return result;
    }

}
