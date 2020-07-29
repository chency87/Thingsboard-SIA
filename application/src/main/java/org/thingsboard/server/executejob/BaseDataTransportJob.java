package org.thingsboard.server.executejob;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.data.id.EntityIdFactory;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.kv.*;
import org.thingsboard.server.controller.DataTransportController;
import org.thingsboard.server.controller.idscontroller.ConstantConfValue;
import org.thingsboard.server.dao.model.sql.MqttEntity;
import org.thingsboard.server.dao.timeseries.BaseTimeseriesService;
import org.thingsboard.server.service.ids.MqttTransportService;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class BaseDataTransportJob implements BaseJob{


    @Autowired
    private MqttTransportService mqttTransportService;

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
        //
        mqttTcpTransport(tenantId.toString(),arrayLastValue.getAsString());

//        System.out.println(arrayLastValue);
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

    private void mqttTcpTransport(String tenantId,String payload){

        List<MqttEntity> mqttEntities = mqttTransportService.configMqttGet(tenantId);
        if (mqttEntities.size() == 0){
            throw new RuntimeException("配置错误");
        }
        MqttEntity mqttEntity = mqttEntities.get(0);
        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setPassword(mqttEntity.getPassword().toCharArray());
            options.setUserName(mqttEntity.getUsername());
            MqttAsyncClient client = new MqttAsyncClient(mqttEntity.getAddress(), ConstantConfValue.CLIENT_ID);
            client.connect(options);
//            Thread.sleep(3000);
            MqttMessage message = new MqttMessage();
//            payload示例：{"key1":"value1", "key2":true, "key3": 3.0, "key4": 4}
            message.setPayload(payload.getBytes());
            client.publish(mqttEntity.getTopic(), message);
            client.disconnect();
            log.info("Disconnected");
            System.exit(0);
        } catch (Exception e) {
            log.error("Unexpected exception occurred in MqttSslClient", e);
        }
    }

}
