package org.thingsboard.server.dao.sql.secgate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thingsboard.server.dao.model.sql.SensorEntity;

public interface SensorRepository extends JpaRepository<SensorEntity, Integer> {
}
