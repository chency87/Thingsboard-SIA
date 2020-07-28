package org.thingsboard.server.dao.sql.secgate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.thingsboard.server.dao.CountSig;
import org.thingsboard.server.dao.model.sql.TcphdrEntity;

import java.util.List;

public interface TcphdrRepoeitory extends JpaRepository<TcphdrEntity, Integer> {

   @Query(value ="select new org.thingsboard.server.dao.sql.secgate.CountTcpPort(count(e.tcpDport),e.tcpDport) from TcphdrEntity as e GROUP BY e.tcpDport ORDER BY e.tcpDport DESC")
    List<CountTcpPort> getTcpPort();

//    List<TcphdrEntity> findAllById();
@Query(value ="select u from TcphdrEntity as u where  u.cid=?1 and u.sid=?2")
    List<TcphdrEntity> findAllByCidAndSid(int cid,int sid);
}
