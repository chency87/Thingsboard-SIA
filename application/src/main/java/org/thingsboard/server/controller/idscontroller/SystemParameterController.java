package org.thingsboard.server.controller.idscontroller;

import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.thingsboard.server.bean.SystemParameter;
import org.thingsboard.server.service.ids.SystemParameterService;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//@Configuration      //1.主要用于标记配置类，兼备Component的效果。
//@EnableScheduling   // 2.开启定时任务

@RestController
@RequestMapping("/api")
public class SystemParameterController {
    @Autowired
    private SystemParameterService systemParameterService;

    @ApiOperation(value="系统的状态",notes="systemName：系统名称；systemVersion：版本；runningTime：运行时间；" +
            "cpuUtilization：CPU利用率；memoryUtilization：内存利用率；swapUtilization：swap利用率" +
            "storageSpaceUtilization：存储空间利用率；numOfDevice：设备个数；numOfProtocol：协议数；numOfDataCache：缓存数据量")
    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/systemparameter", method = RequestMethod.GET)
    @ResponseBody
    public List<SystemParameter> systemParameters() throws Exception {
        return systemParameterService.getSystemParameters();
    }

    /*@Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN', 'CUSTOMER_USER')")
    @RequestMapping(value = "/acid/time", method = RequestMethod.GET)
    @ResponseBody
    public List<SystemParameter> TaskTimer () throws Exception{
        TimerTask timerTask = new TimerTask() {
            @SneakyThrows
            @Override public void run() {
                messagingTemplate.convertAndSend(systemParameterService.getSystemParameters());
            }
        };
        Timer timer = new Timer(); //每10秒执行一次
        timer.schedule(timerTask,10,10000);

        return systemParameterService.getSystemParameters();
    }*/

}
