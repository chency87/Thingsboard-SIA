package org.thingsboard.server.dao.sql.secgate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thingsboard.server.dao.model.sql.DataEntity;

public interface DataRepository extends JpaRepository<DataEntity, Integer> {
}
