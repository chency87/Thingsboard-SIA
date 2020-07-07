package org.thingsboard.server.dao.sql.secgate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.thingsboard.server.dao.model.sql.IphdrEntity;

import java.util.List;

public interface IphdrRepository extends JpaRepository<IphdrEntity, Integer> {

    List<IphdrEntity> findIphdrEntitiesByIpProto(Integer ipProto);
    @Query(value = "SELECT COUNT(DISTINCT(ip_src))  FROM  iphdr;", nativeQuery = true)
    public int queryipsrcCount();
    @Query(value = "SELECT ip_proto FROM iphdr GROUP BY ip_proto;", nativeQuery = true)
    List<Integer> queryIpprogroup();

    @Query(nativeQuery = true, value = "SELECT count(ip_src)\r\n" +
            "FROM iphdr \r\n" +
            "WHERE cid <= ? AND sid = ?")
    long countBySidAndCid(Integer cid, Integer sid);

    List<IphdrEntity> findBySidAndCid(Integer sid, Integer cid);

    @Query(nativeQuery = true, value = "SELECT ip_src FROM iphdr WHERE sid = 1 AND cid <500 GROUP BY (ip_src)")
    List<String> getSRC1();


}
