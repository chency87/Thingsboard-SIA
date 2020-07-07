package org.thingsboard.server.dao.sql.secgate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.thingsboard.server.dao.model.sql.Dashboard1;
import org.thingsboard.server.dao.model.sql.DeviceStatistic;
import org.thingsboard.server.dao.model.sql.DoubleKeys;
import java.util.List;
import java.util.Map;

public interface DashboardsRepository extends JpaRepository<Dashboard1, DoubleKeys>, JpaSpecificationExecutor<Dashboard1> {
    @Query(nativeQuery = true, value = "SELECT SUM(LENGTH(a.data_payload))\r\n" +
            "FROM DATA a LEFT JOIN EVENT b \r\n" +
            "ON a.sid = b.sid AND a.cid = b.cid\r\n" +
            "WHERE b.timestamp >= to_timestamp(?1,'yyyy-MM-dd hh24:mi:ss') AND b.timestamp < to_timestamp(?2,'yyyy-MM-dd hh24:mi:ss')")
    Long getIntervalDataSum(String start, String end);

    @Query(nativeQuery = true, value = "SELECT COUNT(ip_src),ip_src FROM iphdr WHERE sid = 1 AND cid <500 GROUP BY (ip_src)")
    Map<Integer, Object> getSRC();

    @Query(nativeQuery = true, value = "SELECT ip_ver AS ipsrc,COUNT(ip_ver) AS num FROM iphdr WHERE sid = 1 AND cid <500 GROUP BY (ip_ver)")
    List<DeviceStatistic> getSRC1();

    @Query(nativeQuery = true, value = "SELECT ip_src FROM iphdr WHERE sid = 1 AND cid=4")
    Object getSRC12();
}
