package org.thingsboard.server.dao.sql.secgate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thingsboard.server.dao.model.sql.TcphdrEntity;

public interface TcphdrRepoeitory extends JpaRepository<TcphdrEntity, Integer> {
}
