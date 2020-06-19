package org.thingsboard.server.dao.sql.secgate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thingsboard.server.dao.model.sql.IphdrEntity;

import java.util.List;

public interface IphdrRepository extends JpaRepository<IphdrEntity, Integer> {

    List<IphdrEntity> findIphdrEntitiesByIpProto(Integer ipProto);



}
