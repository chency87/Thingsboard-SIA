package org.thingsboard.server.dao.sql.secgate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thingsboard.server.dao.model.sql.MqttEntity;

import java.util.List;

public interface MqttRepository extends JpaRepository<MqttEntity,Integer> {

    List<MqttEntity> findByTenantId(String tenantId);
}
