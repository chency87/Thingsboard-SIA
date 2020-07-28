package org.thingsboard.server.dao.sql.secgate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.thingsboard.server.dao.CountSig;
import org.thingsboard.server.dao.model.sql.IphdrEntity;

import java.util.List;

public interface IphdrRepository extends JpaRepository<IphdrEntity, Integer> {

    @Query(value ="select u from IphdrEntity as u where  u.cid=?1 and u.sid=?2")
    List<IphdrEntity> findAllByCidAndSid(int cid,int sid);

    @Query(value = "SELECT COUNT(ip_proto)  FROM  IphdrEntity WHERE ipProto = :ipProto")
    Integer countByIpProto(@Param("ipProto") Integer ipProto);

    @Query(value = "SELECT COUNT(DISTINCT(ip_src))  FROM  iphdr", nativeQuery = true)
    public int queryipsrcCount();

    @Query(value = "SELECT ip_proto FROM iphdr GROUP BY ip_proto;", nativeQuery = true)
    List<Integer> queryIpprogroup();


/**/
    @Query(value = "SELECT new org.thingsboard.server.dao.sql.secgate.CountSrcIP(count(e.ipSrc),e.ipSrc) FROM IphdrEntity as e GROUP BY e.ipSrc ORDER BY e.ipSrc DESC ")
    List<CountSrcIP> getIPSrcAndCount();

    @Query(value = "SELECT new org.thingsboard.server.dao.sql.secgate.CountDstIP(count(e.ipDst),e.ipDst) FROM IphdrEntity as e GROUP BY e.ipDst ORDER BY e.ipDst DESC ")
    List<CountDstIP> getIPDstAndCount();

    @Query(nativeQuery = true, value = "SELECT count(ip_src)\r\n" +
            "FROM iphdr \r\n" +
            "WHERE cid <= ? AND sid = ?")
    long countBySidAndCid(Integer cid, Integer sid);

    List<IphdrEntity> findBySidAndCid(Integer sid, Integer cid);

    @Query(nativeQuery = true, value = "SELECT ip_src FROM iphdr WHERE sid = 1 AND cid <500 GROUP BY (ip_src)")
    List<String> getSRC1();



}
