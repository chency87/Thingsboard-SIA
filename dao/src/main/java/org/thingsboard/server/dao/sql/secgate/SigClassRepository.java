package org.thingsboard.server.dao.sql.secgate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thingsboard.server.dao.model.sql.SigClassEntity;

public interface SigClassRepository extends JpaRepository<SigClassEntity, Integer> {
}
