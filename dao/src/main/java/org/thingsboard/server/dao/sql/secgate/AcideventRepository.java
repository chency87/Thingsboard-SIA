package org.thingsboard.server.dao.sql.secgate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.thingsboard.server.dao.model.sql.AcideventEntity;
import java.sql.ResultSet;
import java.util.List;

public interface AcideventRepository extends JpaRepository<AcideventEntity, Integer> {
    @Query(value = "SELECT \r\n" +
            "b.`cid` AS Cid,\r\n" +
            "b.`sid` AS Sid,\r\n" +
            "b.`signature` AS Signature,\r\n" +
            "b.`timestamp` AS Timestamps,\r\n" +
            "a.`ip_dst` AS Dst,\r\n" +
            "a.`ip_proto` AS pro,\r\n" +
            "a.`ip_src` AS Src,\r\n" +
            "c.`sig_class_id` AS Sigclassid,\r\n" +
            "c.`sig_name` AS Signame,\r\n" +
            "c.`sig_priority` AS Prio,\r\n" +
            "d.`tcp_dport` AS TDport,\r\n" +
            "d.`tcp_sport` AS TSport,\r\n" +
            " e.`udp_dport` AS UDport,\r\n" +
            " e.`udp_sport` AS USport\r\n" +
            "\r\n" +
            "FROM thingsboard.`event` b LEFT JOIN thingsboard.`iphdr` a ON a.sid = b.sid AND a.cid = b.cid \r\n" +
            " LEFT JOIN thingsboard.`signature` c ON b.`signature`= c.`sig_id`\r\n" +
            " LEFT JOIN thingsboard.tcphdr d ON d.sid = b.sid AND d.cid = b.cid \r\n" +
            "LEFT JOIN thingsboard.udphdr e ON e.sid = b.sid AND e.cid = b.cid ", nativeQuery = true)

    List<Object> Acideventlist();


    @Query(value ="select new org.thingsboard.server.dao.sql.secgate.CountTcpPort(count(e.tcpDport),e.tcpDport) from TcphdrEntity as e GROUP BY e.tcpDport ORDER BY e.tcpDport DESC")
    List<CountTcpPort> getTcpPort();
}
