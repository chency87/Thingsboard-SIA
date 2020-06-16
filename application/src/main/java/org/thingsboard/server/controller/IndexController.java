package org.thingsboard.server.controller;


import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thingsboard.server.plugin.DataFetchPluginManager;
import org.thingsboard.server.plugin.bean.DataFetchPlugin;
import org.thingsboard.server.service.plugin.AsyncService;
import org.thingsboard.server.service.sys.SysInfoService;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import org.thingsboard.server.plugin.bean.SocketMessage;

//@RestController
@Controller
public class IndexController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private AsyncService asyncPluginService;

    @Autowired
    private DataFetchPluginManager dfp;
//    @RequestMapping("/")
//    public String getMsg()
//    {
//        try {
//
//            return dfp.updatePluginList().toString();
//        } catch (DocumentException | MalformedURLException e) {
//            e.printStackTrace();
//        }
//        return "PluginManager.list.toString()";
//    }
    //websocket开始
    @Resource
    private SysInfoService sysInfo;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @RequestMapping("/2")
    public String webSocket1() {
        return "index2";
    }
    @GetMapping("/")
    public String webSocket() {
        return "index";
    }

    @MessageMapping("/send")
    @SendTo("/topic/send")
    public SocketMessage send(SocketMessage message) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        message.date = df.format(new Date());
        return message;
    }

    @Scheduled(fixedRate = 1000)
    @SendTo("/topic/callback")
    public Object callback() throws InterruptedException, SigarException {
        Sigar sigar = new Sigar();
        CpuPerc cpu = sigar.getCpuPerc();
//		sysInfo.printCpuPerc(cpu);
        messagingTemplate.convertAndSend("/topic/callback",sysInfo.printCpuPerc());
        System.out.println(sysInfo.printCpuPerc());
        return "callback";

    }

    //websocket结束
    @RequestMapping("/do")
    public String doAcquire(String entityID){

        //Step 1 根据实体ID获取其全部属性信息
        //Step 2 从设备属性中获取设备所采用的插件名称
        //Step 3 若设备还未设置采用的数采插件或者设备采用的插件已经被停用，则返回错误信息
        //Step 4 若设备已经设置采用的数采插件，则根据该插件名称获取其需要的全部参数信息，并从设备属性信息中查询，对插件需要的参数进行一一赋值
        String pluginName = "A";
        DataFetchPlugin plugin = dfp.getPlginInfoFromList(pluginName);
        Map<String,String> map = new HashMap<String,String>();
        for(int i =0;i<plugin.getRequires().size();i++){
            map.put(plugin.getRequires().get(i),"Attr");
        }

        //Step 5 将插件信息以及其所需要的配置信息做为参数，调用异步执行算法

        //若不为空，则执行下述异步方法
        System.out.println("Start submit Data Fetch task to pool");
        asyncPluginService.executeAsyncService(plugin,map);
        System.out.println("End submit Data Fetch task to pool");
        return "pool test";
    }

    //TODO: 上传或更新协议插件
    @RequestMapping(value = "/put", method = RequestMethod.POST )
    public Boolean putXML(@RequestBody DataFetchPlugin dataFetchPlugin) {
        return dfp.addProto(dataFetchPlugin);
    }

    //TODO: 删除插件
    @RequestMapping(value = "/delete", method = RequestMethod.POST )
    public Boolean deleteXML(@RequestBody DataFetchPlugin dataFetchPlugin) {

        return dfp.deleteProto(dataFetchPlugin);
    }


    //TODO: 上传插件

    @PostMapping("/upload")
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

}
