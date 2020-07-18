package org.thingsboard.server.service.ids;

import org.hyperic.sigar.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.thingsboard.server.bean.SystemParameter;
import org.thingsboard.server.dao.sql.secgate.DataRepository;
import org.thingsboard.server.dao.sql.secgate.IphdrRepository;
import org.thingsboard.server.dao.sql.secgate.UdphdrRepository;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//@Component
@Service
public class SystemParameterService {
    @Autowired
    private IphdrRepository iphdrDao;
    @Autowired
    private UdphdrRepository udphdrDao;
    @Autowired
    private DataRepository dataDao;

    public List<SystemParameter> getSystemParameters() throws Exception {
        List<SystemParameter>  list = new ArrayList<>();
        SystemParameter systemParameter = new SystemParameter();
        systemParameter.setCpuUtilization(getCPURate());
        systemParameter.setMemoryUtilization(Memory());
        systemParameter.setNumOfDataCache(cacheData());
        systemParameter.setNumOfDevice(querydevicecount());
        systemParameter.setNumOfProtocol(proCount());
        systemParameter.setRunningTime(getSystemUptime());
        systemParameter.setStorageSpaceUtilization(getDiskInfo());
        systemParameter.setSwapUtilization(getSwap());
        systemParameter.setSystemName(System.getProperty("os.name"));
        systemParameter.setSystemVersion(System.getProperty("os.version"));
        list.add(systemParameter);
        return list;
    }
    private static String osName = System.getProperty("os.name");
    /***
     * 获取系统运行时间
     */
    public static long getSystemUptime() throws Exception {
        long uptime = 0;
        String os = System.getProperty("os.name").toLowerCase();
        System.out.println(os);
        if (os.contains("win")) {
//            net stats srv
            Process uptimeProc = Runtime.getRuntime().exec("systeminfo |find \"系统启动时间:\"");
//            System.out.println(uptimeProc);
            BufferedReader in = new BufferedReader(new InputStreamReader(uptimeProc.getInputStream()));
//            System.out.println(in);
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
                if (line.startsWith("Statistics since")) {
                    SimpleDateFormat format = new SimpleDateFormat("'Statistics since' MM/dd/yyyy hh:mm:ss a");
                    Date boottime = format.parse(line);
                    uptime = System.currentTimeMillis() - boottime.getTime();
                    break;
                }
            }

        } else if (os.contains("mac") || os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            Process uptimeProc = Runtime.getRuntime().exec("uptime");
            BufferedReader in = new BufferedReader(new InputStreamReader(uptimeProc.getInputStream()));
            String line = in.readLine();
            if (line != null) {
                Pattern parse = Pattern.compile("((\\d+) days,)? (\\d+):(\\d+)");
                Matcher matcher = parse.matcher(line);
                if (matcher.find()) {
                    String _days = matcher.group(2);
                    String _hours = matcher.group(3);
                    String _minutes = matcher.group(4);
                    int days = _days != null ? Integer.parseInt(_days) : 0;
                    int hours = _hours != null ? Integer.parseInt(_hours) : 0;
                    int minutes = _minutes != null ? Integer.parseInt(_minutes) : 0;
                    uptime = (minutes * 60000) + (hours * 60000 * 60) + (days * 6000 * 60 * 24);
                }
            }
        }
        return uptime;
    }
    /***
     * 获取swap使用率
     */
    public String getSwap(){
        // a)物理内存信息
        DecimalFormat df = new DecimalFormat("#0.00");
        Sigar sigar = new Sigar();
        String a = null;
        Mem mem;
        try {
            mem = sigar.getMem();

            Swap swap = sigar.getSwap();
//            "交换区总量"
            a=(float)swap.getUsed()/(float)swap.getTotal()*100+"%";
        } catch (SigarException e) {
            e.printStackTrace();
        }
        return a;
    }
    /***
     * 获取存储空间使用率
     */
    public String getDiskInfo(){
        File[] disks = File.listRoots();
        Double sum = 0.0;
        Double use = 0.0;
        for(File file : disks)
        {
            use += file.getUsableSpace() / 1024 / 1024;// 可用空间
            sum += file.getTotalSpace() / 1024 / 1024 ;// 总空间
        }
        return use/sum*100+"%";
    }
    /***
     * 获取cpu平均值使用率
     */
    public static Double getCPURate() {
        Double CPURate = 0.0;
        Double CPURate1 = 0.0;
        try {
            Sigar sigar = new Sigar();
            CpuInfo infos[] = new CpuInfo[0];
            infos = sigar.getCpuInfoList();
            CpuPerc cpuList[] = null;
            cpuList = sigar.getCpuPercList();
            for (int i = 0; i < infos.length; i++) {// 不管是单块CPU还是多CPU都适用
                CpuInfo info = infos[i];
                CPURate += cpuList[i].getCombined();
            }
            CPURate1 = CPURate / infos.length;
        } catch (SigarException e) {
            e.printStackTrace();
        }
        CPURate1 = (double) Math.round(CPURate1 * 100);
        return CPURate1;
    }

    /***
     *  获取内存使用率
     */
    public static String Memory(){
        if (osName.toLowerCase().contains("windows") || osName.toLowerCase().contains("win")) {
            String memory = getMemery();
            return memory;
        } else {
            String memory = getMemUsage()+"";
            return memory;
        }
    }
    /**
     * 获取windows内存使用率
     */
    public static String getMemery() {
        String format = "";
        try {
            DecimalFormat df = new DecimalFormat("#.00");
            Sigar sigar = new Sigar();
            Mem mem = sigar.getMem();
            float total = mem.getTotal() / 1024L;
            float used = mem.getUsed() / 1024L;
            format = df.format((used/total)*100);
            return  format;
        } catch (SigarException e) {
            e.printStackTrace();
        }
        return  format;
    }
    /***
     * 获取linux内存使用率
     */
    public static String getMemUsage() {
        Map<String, Object> map = new HashMap<String, Object>();
        InputStreamReader inputs = null;
        BufferedReader buffer = null;
        try {
            inputs = new InputStreamReader(new FileInputStream("/proc/meminfo"));
            buffer = new BufferedReader(inputs);
            String line = "";
            while (true) {
                line = buffer.readLine();
                if (line == null)
                    break;
                int beginIndex = 0;
                int endIndex = line.indexOf(":");
                if (endIndex != -1) {
                    String key = line.substring(beginIndex, endIndex);
                    beginIndex = endIndex + 1;
                    endIndex = line.length();
                    String memory = line.substring(beginIndex, endIndex);
                    String value = memory.replace("kB", "").trim();
                    map.put(key, value);
                }
            }
            long memTotal = Long.parseLong(map.get("MemTotal").toString());
            long memFree = Long.parseLong(map.get("MemFree").toString());
            long memused = memTotal - memFree;
            long buffers = Long.parseLong(map.get("Buffers").toString());
            long cached = Long.parseLong(map.get("Cached").toString());
            double usage = (double) (memused-buffers-cached) / memTotal * 100;
            DecimalFormat df = new DecimalFormat("#.00");
            String format = df.format(usage);
            return format;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                buffer.close();
                inputs.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return "0";
    }
    public int querydevicecount() {
        // TODO Auto-generated method stub
        return iphdrDao.queryipsrcCount();
    }



    public int proCount() {
        // TODO Auto-generated method stub
        int sum=0;
        List<Integer> ii = iphdrDao.queryIpprogroup();
        for(int i=0; i<ii.size(); i++) {
//            Integer c = ii.get(i);
            Number num = (Number) ii.get(i);
            Integer c=num.intValue();
            if(c==1) {
                sum = udphdrDao.queryTcpUdpport()+1;
                break;
            }
            else {
                sum = udphdrDao.queryTcpUdpport();
            }
        }
        return sum;
    }


    public double cacheData() {
        // TODO Auto-generated method stub
        return dataDao.queryCachedataCount();
    }
}
