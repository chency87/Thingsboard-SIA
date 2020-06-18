package org.thingsboard.server.dao.sql.secgate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thingsboard.server.dao.model.sql.SignatureEntity;

public interface SignatureRepository extends JpaRepository<SignatureEntity, Integer> {
}
