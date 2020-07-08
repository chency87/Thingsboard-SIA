package org.thingsboard.server.dao.sql.secgate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.thingsboard.server.dao.model.sql.IcmphdrEntity;

public interface IcmphdrRepository extends JpaRepository<IcmphdrEntity, Integer> {
    @Query(value = "SELECT (SELECT COUNT(DISTINCT(tcp_dport))FROM tcphdr) + (SELECT COUNT(DISTINCT(udp_dport))FROM udphdr) TOTAL;", nativeQuery = true)
    int queryTcpUdpport();
}
