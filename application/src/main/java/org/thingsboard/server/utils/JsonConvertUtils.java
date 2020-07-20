package org.thingsboard.server.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.kafka.common.protocol.types.Field;
import org.thingsboard.server.controller.DataTransportController;
import springfox.documentation.spring.web.json.Json;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

public class JsonConvertUtils {
    public static void main(String[] args){
        String first = "[{\"id\":{\"entityType\":\"DEVICE\",\"id\":\"85cacf50-bb6a-11ea-8cc4-73c0690c1ed2\"},\"keys\":[{\"key\":\"key4\",\"scope\":\"(*,*)\"},{\"key\":\"key1\",\"scope\":\"(*,*)\"},{\"key\":\"v1\",\"scope\":\"(*,*)\"}]},{\"id\":{\"entityType\":\"DEVICE\",\"id\":\"85cacf50-bb6a-11ea\"},\"keys\":[{\"key\":\"key4\",\"scope\":\"(*,*)\"},{\"key\":\"key5\",\"scope\":\"(*,*)\"},{\"key\":\"v3\",\"scope\":\"(*,*)\"}]}]";
        String second = "[{\"id\":{\"entityType\":\"DEVICE\",\"id\":\"85cacf50-bb6a-11ea-8cc4-73c0690c1ed2\"},\"keys\":[{\"key\":\"key2\",\"scope\":\"(*,*)\"},{\"key\":\"key1\",\"scope\":\"(*,*)\"},{\"key\":\"v1\",\"scope\":\"(*,*)\"},{\"key\":\"v3\",\"scope\":\"(*,*)\"},{\"key\":\"v2\",\"scope\":\"(*,*)\"}]}]";
        System.out.println(first);
        String three = "{\"id\":{\"entityType\":\"DEVICE\",\"id\":\"85cacf50-bb6a-11ea-8cc4-73c0690c1ed2\"},\"keys\":[{\"key\":\"key2\",\"scope\":\"(*,*)\"},{\"key\":\"key1\",\"scope\":\"(*,*)\"}]}";
        String four = "{\"id\":{\"entityType\":\"DEVICE\",\"id\":\"85cacf50-bb6a-11ea-8cc4-73c0690c1ed2\"},\"keys\":[{\"key\":\"v1\",\"scope\":\"(*,*)\"},{\"key\":\"v2\",\"scope\":\"(*,*)\"}]}";
        String five = "{\"id\":{\"entityType\":\"DEVICE\",\"id\":\"85cacf50-bb6a-11ea-8cc4-73c0690c1ed2\"},\"keys\":[{}]}";
        Map<String,Map<String,String>> re = parseTransportParams(first);
//        updateTransportationParamsMap("tenantId",first);
        updateTransportationParamsMap("tenantId",four);
        System.out.println(DataTransportController.transportationParamsMap);
//        updateTransportationParamsMap("tenantId",five);
        releaseTransportationParamsMap("tenantId",four);
//        releaseTransportationParamsMap("tenantId",first);
        System.out.println(DataTransportController.transportationParamsMap);
    }

    public static void releaseTransportationParamsMap(String tenantId, String json){
        Map<String,Map<String,String>> map = parseTransportParams(json);
        if(DataTransportController.transportationParamsMap.get(tenantId) == null){
            return;
        }
        Map<String,Map<String,String>> currentMap = DataTransportController.transportationParamsMap.get(tenantId);
        for(Map.Entry<String,Map<String,String>> entry : map.entrySet()){
            if(currentMap.keySet().contains(entry.getKey())){
                for(Map.Entry<String,String> keyAndScope : entry.getValue().entrySet()){
                    currentMap.get(entry.getKey()).remove(keyAndScope.getKey());
                }
            }
        }
        Iterator<Map.Entry<String,Map<String,String>>> it = currentMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String,Map<String,String>> entry = it.next();
            if(entry.getValue().isEmpty()){
                it.remove();//使用迭代器的remove()方法删除元素
            }
        }
        if(currentMap.isEmpty()){
            DataTransportController.transportationParamsMap.remove(tenantId);
        }
//
    }

    public static void updateTransportationParamsMap(String tenantId, String json){
        Map<String,Map<String,String>> map = parseTransportParams(json);
        if(DataTransportController.transportationParamsMap.get(tenantId) == null){
            DataTransportController.transportationParamsMap.put(tenantId,map);
            return;
        }
        Map<String,Map<String,String>> currentMap = DataTransportController.transportationParamsMap.get(tenantId);
        for(Map.Entry<String,Map<String,String>> entry : map.entrySet()){
            if(currentMap.keySet().contains(entry.getKey())){
                currentMap.get(entry.getKey()).putAll(entry.getValue());
            }
        }
        DataTransportController.transportationParamsMap.put(tenantId,currentMap);
    }
    public static Map<String,Map<String,String>> parseTransportParams(String json){
        Map<String,Map<String,String>> currentUserParamsMap =  new HashMap<>();;
        JsonElement element = new JsonParser().parse(json);
        if(element.isJsonArray()){
            JsonArray array = element.getAsJsonArray();
            for(int i = 0;i<array.size();i++){
                JsonObject object = array.get(i).getAsJsonObject();
                String entityId = object.get("id").getAsJsonObject().get("id").getAsString();
                currentUserParamsMap.put(entityId,parseKeyAdnScopeItem(object));
//                System.out.println("tmp +  " + currentUserParamsMap);
            }
        }else if(element.isJsonObject()){
            JsonObject object = element.getAsJsonObject();
            String entityId = object.get("id").getAsJsonObject().get("id").getAsString();
            currentUserParamsMap.put(entityId,parseKeyAdnScopeItem(object));
        }
        return currentUserParamsMap;
    }


    private static Map<String,String> parseKeyAdnScopeItem(JsonObject object){
        JsonElement element = object.get("keys");
        Map<String,String> keyAndScope = new HashMap<>();
        if(element.isJsonArray() && element.toString() !="[{}]"){
            JsonArray keys = element.getAsJsonArray();
            for(int i = 0;i<keys.size();i++){
                if(keys.get(i).toString().trim() != "{}" && keys.get(i).getAsJsonObject().entrySet().size()> 0){
                    String keyName = keys.get(i).getAsJsonObject().get("key").getAsString();
                    String scope= keys.get(i).getAsJsonObject().get("scope").getAsString();
                    keyAndScope.put(keyName,scope);
                }
            }
        }else  if(element.isJsonObject()){
            if(element.toString().trim() != "{}" && element.getAsJsonObject().entrySet().size()> 0){
                String keyName = element.getAsJsonObject().get("key").getAsString();
                String scope= element.getAsJsonObject().get("scope").getAsString();
                keyAndScope.put(keyName,scope);
            }
        }
        return keyAndScope;
    }
}


//JSON 格式
//       [{
//            id:{
//			     "entityType": "DEVICE",
//			     "id": "ff06bb60-7c84-11ea-863a-b53e4a73af9b"
//		         },
//            keys:[{
//                key:"key1",
//                scope:"[*,*]"
//                },{
//                key:"key2",
//                scope:"[*,*]"
//                }]
//        },{
//            id:{
//			     "entityType": "DEVICE",
//			     "id": "ff06bb60-7c84-11ea-863a-b53e4a73af9b"
//		         },
//            keys:[{
//                key:"key3",
//                scope:"[*,*]"
//                },{
//                key:"key1",
//                scope:"[*,*]"
//                }]
//        }]
