package org.thingsboard.server.dao.sql.secgate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thingsboard.server.dao.model.sql.OptEntity;

public interface OptRepository extends JpaRepository<OptEntity, Integer> {
}
