package org.thingsboard.server.dao.sql.secgate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.thingsboard.server.dao.model.sql.IphdrEntity;

import java.util.List;

public interface IphdrRepository extends JpaRepository<IphdrEntity, Integer> {

    List<IphdrEntity> findIphdrEntitiesByIpProto(Integer ipProto);

    @Query(value = "SELECT count(ip_src),ip_src FROM iphdr GROUP BY ip_src ORDER BY ip_src DESC LIMIT 5", nativeQuery = true)
    List getIPSrcAndCount();


}
