package org.thingsboard.server.service.plugin;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import cn.sia.sec.proto.plugin.AsyncDataFetchPluginService;
import org.thingsboard.server.plugin.DataFetchPluginManager;
import org.thingsboard.server.plugin.bean.DataFetchPlugin;

import javax.annotation.Resource;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

@Service
public class AsyncDataFetchServiceImpl implements AsyncService {

    @Autowired
    private DataFetchPluginManager dfm;
    @Resource(name = "defaultThreadPool")
    private ThreadPoolTaskExecutor poolTaskExecutor;

    private DataFetchPlugin plugin;

    private Map<String, String> config ;

    public DataFetchPlugin getPlugin() {
        return plugin;
    }

    public void setPlugin(DataFetchPlugin plugin) {
        this.plugin = plugin;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

    @Override
    //@Async("defaultThreadPool")
    public Boolean executeAsyncService(DataFetchPlugin plugin, Map<String,String> config) {

        if ((plugin != null)  && (config !=null)){
            System.out.println("========---" + plugin);
            System.out.println("========---" +config);
            setConfig(config);
            setPlugin(plugin);
            System.out.println("==getConfig======---" + getConfig());
            System.out.println("====getPlugin====---" +getPlugin());
            return true;
        }


     /*   while (plugin.getStatus()) {

            System.out.println(" Start Execute the Service ");
            try {
                AsyncDataFetchPluginService service = dfm.getInstanceByClassName(plugin.getClassName());
                service.executeAsyncService(config);

                System.out.println("Stop Execute the Service");
                return true;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return false;
        }*/
        return false;
    }






    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {


        Map<String, String> config = this.getConfig();
        DataFetchPlugin plugin = this.getPlugin();

        System.out.println("========---" + plugin);
        System.out.println("========---" +config);
//        String jarPath = "D:/cn.sia.sec-1.0-SNAPSHOT.jar";
        File jarFile = new File(plugin.getName());
//        String className = "cn.sia.sec.proto.plugin.ModbusTcpServiceImpl";
        URL url = null;
        try {
            url = jarFile.toURI().toURL();
            ClassLoader system = new URLClassLoader(new URL[] { url }, Thread.currentThread().getContextClassLoader());
            Class<?> cs = system.loadClass(plugin.getClassName());
            Object object = cs.newInstance();
//            Map<String,String> map = new HashMap<String,String>();
//            map.put("port","502");
//            map.put("ip","127.0.0.1");
//            map.put("funCode","1");
//            map.put("slaveId","1");
//            map.put("address","0");
//            map.put("quantity","4");
//            map.put("attr","od,op,dp,di");
            AsyncDataFetchPluginService service = (AsyncDataFetchPluginService)cs.newInstance();//dfm.getInstanceByClassName(plugin.getClassName());
            System.out.println(service.executeAsyncService(this.config));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        System.out.println("定时启动");
//        AsyncDataFetchPluginService service = null;
//        try {
////            service = dfm.getInstanceByClassName(plugin.getClassName());
//            service = dfm.getInstanceByClassName("ModbusTCP2");
//            Map<String, String> stringStringMap = service.executeAsyncService(config);
//            System.out.println(stringStringMap.toString());
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
    }
}
