package org.thingsboard.server.controller;
import java.util.*;

import org.hyperic.sigar.CpuPerc;
import javax.annotation.Resource;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.thingsboard.server.service.sys.SysInfoService;

import lombok.extern.log4j.Log4j2;
@RestController
@Log4j2
//
@Configuration
@EnableWebSocket
//
public class SysInfoController {

	@Resource
	private SysInfoService sysInfo;


/*	@RequestMapping(value = "/webSocket/cpu")
	List<Object> printCpuPerc() throws InterruptedException, SigarException {
		
        return sysInfo.printCpuPerc();
       
	}
	@RequestMapping(value = "/sys/phymeo")
	List<Object> getPhysicalMemory()  {
		return sysInfo.getPhysicalMemory();
	}
	@RequestMapping(value = "/sys/who")
	List<Object> Who()  {
		return sysInfo.testWho();
	}
	@RequestMapping(value = "/sys/file")
	List<Object> FileSystemInfo() throws Exception  {
		return sysInfo.testFileSystemInfo();
	}
	*/
}
