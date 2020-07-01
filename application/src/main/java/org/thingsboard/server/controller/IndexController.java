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
import org.thingsboard.server.bean.SocketMessage;
import org.thingsboard.server.plugin.DataFetchProtoHandlePluginManager;
import org.thingsboard.server.plugin.bean.DataFetchPlugin;
import org.thingsboard.server.service.plugin.AsyncService;
import org.thingsboard.server.service.sys.SysInfoService;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

//@RestController
@Controller
public class IndexController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private AsyncService asyncPluginService;

    @Autowired
    private DataFetchProtoHandlePluginManager dfp;
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


}
