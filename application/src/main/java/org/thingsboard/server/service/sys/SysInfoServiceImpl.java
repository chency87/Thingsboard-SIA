package org.thingsboard.server.service.sys;

import org.hyperic.sigar.CpuPerc;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.thingsboard.server.service.sys.SysInfoService;
import org.thingsboard.server.utils.WebSockets;
import lombok.extern.log4j.Log4j2;

import java.net.InetAddress;  
import java.net.UnknownHostException;  
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hyperic.sigar.CpuInfo;  
 
import org.hyperic.sigar.FileSystem;  
import org.hyperic.sigar.FileSystemUsage;  
import org.hyperic.sigar.Mem;  
import org.hyperic.sigar.NetFlags;  
import org.hyperic.sigar.NetInterfaceConfig;  
import org.hyperic.sigar.NetInterfaceStat;  
import org.hyperic.sigar.OperatingSystem;  
import org.hyperic.sigar.Sigar;  
import org.hyperic.sigar.SigarException;  
import org.hyperic.sigar.SigarNotImplementedException;  
import org.hyperic.sigar.Swap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;  

@Service

public class SysInfoServiceImpl implements SysInfoService {


	private static final Object[][] Object = null;
    // c)CPU的用户使用量、系统使用剩余量、总的剩余量、总的使用占用量等（单位：100%）  

	@Override
	@Scheduled(fixedRate = 5*1000)
	public List<Object> printCpuPerc() throws InterruptedException,SigarException {
		 Sigar sigar = new Sigar();  
		   // 方式一，主要是针对一块CPU的情况  
	        CpuPerc cpu = sigar.getCpuPerc();
	/*        try {
				CpuPerc cpu = sigar.getCpuPerc();
//	            printCpuPerc(cpu);  
	        } catch (SigarException e) {  
	            e.printStackTrace();  
	        }*/
		
	// TODO Auto-generated method stub
		List<Object>  list = new ArrayList<Object> ();
//			Object[] rowArray = Object[6];
		Map<String, Object> mapArr = new HashMap<String, Object>();
	    mapArr.put("用户使用率", CpuPerc.format(cpu.getUser()));
	    mapArr.put("系统使用率", CpuPerc.format(cpu.getSys()));
	    mapArr.put("当前等待率", CpuPerc.format(cpu.getWait()));
	    mapArr.put("Nice", CpuPerc.format(cpu.getNice()));
//	    进程的nice优先级值
	    mapArr.put("当前空闲率", CpuPerc.format(cpu.getIdle()));
	    mapArr.put("总的使用率", CpuPerc.format(cpu.getCombined()));
    	list.add(mapArr);
//    	System.out.println(cpu.getUser());
	    return  list;
	}

	@Override
	public List<Object> getPhysicalMemory() {
		// a)物理内存信息  
        DecimalFormat df = new DecimalFormat("#0.00");  
        Sigar sigar = new Sigar();  
        Mem mem;  
        List<Object>  list = new ArrayList<Object> ();
//		Object[] rowArray = Object[6];
		Map<String, Object> mapArr = new HashMap<String, Object>();
        try {  
            mem = sigar.getMem();  
             
            Swap swap = sigar.getSwap();  

    	    mapArr.put("内存总量", df.format((float)mem.getTotal() / 1024/1024/1024) + "G");
    	    mapArr.put("当前内存使用量", df.format((float)mem.getFree() / 1024/1024/1024) + "G");
    	    mapArr.put("交换区总量", df.format((float)swap.getTotal() / 1024/1024/1024) + "G");
    	    mapArr.put("当前交换区使用量", df.format((float)swap.getUsed() / 1024/1024/1024) + "G");
    	    mapArr.put("当前交换区剩余量", df.format((float)swap.getFree() / 1024/1024/1024) + "G");
    	    list.add(mapArr);
        } catch (SigarException e) {  
            e.printStackTrace();  
        }  
		return  list;
	}

	@Override
	public List<Object> testWho() {
		 List<Object>  list = new ArrayList<Object> ();
 		Map<String, Object> mapArr = new HashMap<String, Object>();
		 try {  
	            Sigar sigar = new Sigar();  
	            org.hyperic.sigar.Who[] who = sigar.getWhoList();  
	           
	            if (who != null && who.length > 0) {  
	                for (int i = 0; i < who.length; i++) { 
	                	mapArr.put("ID", String.valueOf(i));
	                	org.hyperic.sigar.Who _who = who[i];  
	            	    mapArr.put("设备", _who.getDevice());
	            	    mapArr.put("主机", _who.getHost());
	            	    mapArr.put("时间", _who.getTime());
	            	    mapArr.put("用户", _who.getUser());
	            	    list.add(mapArr);
	                } 
	            }  

	        } catch (SigarException e) {  
	            e.printStackTrace();  
	        }
         return  list;
	}

	@Override
	public List<Object> testFileSystemInfo() throws Exception {
		Sigar sigar = new Sigar();  
        FileSystem fslist[] = sigar.getFileSystemList();  
        DecimalFormat df = new DecimalFormat("#0.00");  
        List<Object>  list = new ArrayList<Object> ();
 		Map<String, Object> mapArr = new HashMap<String, Object>();
        // String dir = System.getProperty("user.home");// 当前用户文件夹路径  
        for (int i = 0; i < fslist.length; i++) {  
        	mapArr.put("ID", i);
            FileSystem fs = fslist[i];  
            // 分区的盘符名称  
            mapArr.put("盘符名称", fs.getDevName());
            // 文件系统类型，比如 FAT32、NTFS 
//           mapArr.put("系统类型", fs.getSysTypeName());
            // 文件系统类型名，比如本地硬盘、光驱、网络文件系统等  
            mapArr.put("系统类型", fs.getTypeName());
            // 文件系统类型  
//            System.out.println("fs.getType() = " + fs.getType());  
            FileSystemUsage usage = null;  
            try {  
                usage = sigar.getFileSystemUsage(fs.getDirName());  
            } catch (SigarException e) {  
                if (fs.getType() == 2)  
                    throw e;  
                continue;  
            }  
            switch (fs.getType()) {  
            case 0: // TYPE_UNKNOWN ：未知  
                break;  
            case 1: // TYPE_NONE  
                break;  
            case 2: // TYPE_LOCAL_DISK : 本地硬盘  
                // 文件系统总大小
            	mapArr.put("Total", df.format((float)usage.getTotal()/1024/1024));
//                System.out.println(" Total = " + df.format((float)usage.getTotal()/1024/1024) + "G");  
                // 文件系统剩余大小 
            	mapArr.put("Free", df.format((float)usage.getFree()/1024/1024));
//                System.out.println(" Free = " + df.format((float)usage.getFree()/1024/1024) + "G");  
                // 文件系统可用大小 
            	mapArr.put("Avail", df.format((float)usage.getAvail()/1024/1024));

//                System.out.println(" Avail = " + df.format((float)usage.getAvail()/1024/1024) + "G");  
//                // 文件系统已经使用量  
//                System.out.println(" Used = " + df.format((float)usage.getUsed()/1024/1024) + "G");  
                double usePercent = usage.getUsePercent() * 100D;  
                // 文件系统资源的利用率  
                mapArr.put("Usage", df.format(usePercent) + "%"); 
                break;  
            case 3:// TYPE_NETWORK ：网络  
                break;  
            case 4:// TYPE_RAM_DISK ：闪存  
                break;  
            case 5:// TYPE_CDROM ：光驱  
                break;  
            case 6:// TYPE_SWAP ：页面交换  
                break;
            } 
            list.add(mapArr);
//            System.out.println(" DiskReads = " + usage.getDiskReads());  
//            System.out.println(" DiskWrites = " + usage.getDiskWrites());  
        }  
		// TODO Auto-generated method stub
       
    	return  list;
	}

}
