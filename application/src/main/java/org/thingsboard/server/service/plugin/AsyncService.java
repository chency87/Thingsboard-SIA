package org.thingsboard.server.service.plugin;



import org.thingsboard.server.plugin.bean.DataFetchPlugin;

import java.util.Map;

public interface AsyncService {
    public Boolean executeAsyncService(DataFetchPlugin plugin, Map<String, String> config);
}
