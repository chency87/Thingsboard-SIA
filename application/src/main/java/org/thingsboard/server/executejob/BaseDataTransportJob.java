package org.thingsboard.server.executejob;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.data.id.EntityIdFactory;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.kv.*;
import org.thingsboard.server.controller.DataTransportController;
import org.thingsboard.server.controller.idscontroller.ConstantConfValue;
import org.thingsboard.server.dao.timeseries.BaseTimeseriesService;
import org.thingsboard.server.service.security.model.SecurityUser;
import org.thingsboard.server.utils.JsonConvertUtils;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class BaseDataTransportJob implements BaseJob{



    @Autowired
    private BaseTimeseriesService timeseriesService;

    private List<String> getKeySetStr(Map<String,String> keysAndScope){
        List<String> keySets = new ArrayList<>();
        for(Map.Entry<String,String> entry : keysAndScope.entrySet()){
            keySets.add(entry.getKey());
        }
        return keySets;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JsonArray arrayLastValue = new JsonArray();
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        TenantId tenantId = (TenantId)jobDataMap.get("tenantId");
        Map<String,Map<String,String>> params = DataTransportController.transportationParamsMap.get(tenantId.toString());
        for(Map.Entry<String,Map<String,String>> entry : params.entrySet()){
            JsonObject object = new JsonObject();
            String entityId = entry.getKey();
            Map<String,String> keysAndScope = entry.getValue();
            List<String> keySets = getKeySetStr(keysAndScope);
            EntityId entity = EntityIdFactory.getByTypeAndId("DEVICE", entityId);
            ListenableFuture<List<TsKvEntry>> lastVal = timeseriesService.findLatest(tenantId,entity,keySets);
            try {
                if(lastVal.get().size()!=0){
                    int span = 1000;
                    JsonArray array = initLastValue(lastVal.get(),span);
                    if(array != null && array.size() > 0){
                        object.add("data",array);
                        object.addProperty("id",entity.toString());

                        arrayLastValue.add(object);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.println(arrayLastValue);
    }

    private JsonArray initLastValue(List<TsKvEntry> lastKvEntries , int span){
        JsonArray array = new JsonArray();
        long current = System.currentTimeMillis();
        for (TsKvEntry entry: lastKvEntries) {
            if(current - entry.getTs() > span || entry.getValue() == null){
                continue;
            }
            JsonObject object = new JsonObject();
            switch (entry.getDataType()){
                case STRING:
                    object.addProperty(entry.getKey(),entry.getValueAsString());
                    break;
                case LONG:
                    object.addProperty(entry.getKey(),entry.getLongValue().get());
                    break;
                case BOOLEAN:
                    object.addProperty(entry.getKey(),entry.getBooleanValue().get());
                    break;
                case DOUBLE:
                    object.addProperty(entry.getKey(),entry.getDoubleValue().get());
                    break;
            }
            object.addProperty("ts",entry.getTs());
            array.add(object);
        }
        return array;
    }
}
