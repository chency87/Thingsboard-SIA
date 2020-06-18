package org.thingsboard.server.controller;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.thingsboard.server.bean.SocketMessage;
import org.thingsboard.server.service.sys.SysInfoService;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebsocketController {
    //websocket开始
    @Resource
    private SysInfoService sysInfo;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/webSocket")
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
//        System.out.println(sysInfo.printCpuPerc());
        return "callback";

    }

    //websocket结束
}
