package org.thingsboard.server.dao.sql.secgate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thingsboard.server.dao.model.sql.UdphdrEntity;

public interface UdphdrRepository extends JpaRepository<UdphdrEntity, Integer> {
}
