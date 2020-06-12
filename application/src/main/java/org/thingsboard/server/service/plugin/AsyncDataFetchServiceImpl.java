package org.thingsboard.server.service.plugin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.thingsboard.server.plugin.AsyncDataFetchPluginService;
import org.thingsboard.server.plugin.DataFetchPluginManager;
import org.thingsboard.server.plugin.bean.DataFetchPlugin;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class AsyncDataFetchServiceImpl implements AsyncService {

    @Autowired
    private DataFetchPluginManager dfm;
    @Resource(name = "defaultThreadPool")
    private ThreadPoolTaskExecutor poolTaskExecutor;

    @Override
    @Async("defaultThreadPool")
    public Boolean executeAsyncService(DataFetchPlugin plugin, Map<String,String> config) {

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
    }
}
