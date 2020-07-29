package org.thingsboard.server.service.ids;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thingsboard.server.dao.model.sql.MqttEntity;
import org.thingsboard.server.dao.sql.secgate.MqttRepository;

import java.util.List;

@Service
public class MqttTransportService {

    @Autowired
    private MqttRepository mqttRepository;

    @Transactional(rollbackFor = Exception.class)
    public boolean configMqttSave(MqttEntity mqttConfig) throws Exception{
        MqttEntity save = mqttRepository.save(mqttConfig);
        if (save != null){
            return true;
        }
        return false;
    }

    public  List<MqttEntity> configMqttGet(String tenantId) {
        return mqttRepository.findByTenantId(tenantId);
    }
}
