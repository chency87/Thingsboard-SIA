package org.thingsboard.server.service.sys;

import java.util.List;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.SigarException;

public interface SysInfoService {
	// c)CPU的用户使用量、系统使用剩余量、总的剩余量、总的使用占用量等（单位：100%）  
	public List<Object> printCpuPerc() throws InterruptedException, SigarException;
	public List<Object> getPhysicalMemory();  
	        // a)物理内存信息  
    // c)取当前系统进程表中的用户信息  
    public List<Object> testWho();
    // a)取硬盘已有的分区及其详细信息（通过sigar.getFileSystemList()来获得FileSystem列表对象，然后对其进行编历）：  
    public List<Object> testFileSystemInfo() throws Exception;
}
