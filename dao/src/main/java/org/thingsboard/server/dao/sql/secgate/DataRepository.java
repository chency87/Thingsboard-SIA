package org.thingsboard.server.dao.sql.secgate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.thingsboard.server.dao.model.sql.DataEntity;

public interface DataRepository extends JpaRepository<DataEntity, Integer> {
    @Query(value = "SELECT SUM(LENGTH(data_payload))/(8*1024) FROM data;", nativeQuery = true)
    double queryCachedataCount();
}
