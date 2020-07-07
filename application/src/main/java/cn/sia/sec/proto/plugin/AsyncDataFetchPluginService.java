package cn.sia.sec.proto.plugin;

import java.util.Map;


public interface AsyncDataFetchPluginService {
    Map<String,String> executeAsyncService(Map<String, String> configs);
}



