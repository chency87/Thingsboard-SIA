package org.thingsboard.server.dao.sql.secgate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.thingsboard.server.dao.CountSig;
import org.thingsboard.server.dao.model.sql.UdphdrEntity;

import java.util.List;

public interface UdphdrRepository extends JpaRepository<UdphdrEntity, Integer> {
    @Query(value = "SELECT (SELECT COUNT(DISTINCT(tcp_dport))FROM tcphdr) + (SELECT COUNT(DISTINCT(udp_dport))FROM udphdr) TOTAL;", nativeQuery = true)
    int queryTcpUdpport();

    @Query(value ="select new org.thingsboard.server.dao.sql.secgate.CountUdpPort(count(e.udpDport),e.udpDport) from UdphdrEntity as e GROUP BY e.udpDport ORDER BY e.udpDport DESC")
    List<CountUdpPort> getUdpPort();

    @Query(value ="select u from UdphdrEntity as u where  u.cid=?1 and u.sid=?2")
    List<UdphdrEntity> findAllByCidAndSid(int cid,int sid);

}
