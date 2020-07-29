package org.thingsboard.server.dao.sql.secgate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.thingsboard.server.dao.model.sql.SignatureEntity;

import java.util.List;

public interface SignatureRepository extends JpaRepository<SignatureEntity, Integer> {
    @Query(value ="select u from SignatureEntity as u where  u.sig_id=?1 ")
    List<SignatureEntity> findAllBySig_id(Integer sigid);
}
