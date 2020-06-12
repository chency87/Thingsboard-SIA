package org.thingsboard.server.plugin;

import java.util.Map;

public interface AsyncDataFetchPluginService {
    void executeAsyncService(Map<String, String> configs);

}
