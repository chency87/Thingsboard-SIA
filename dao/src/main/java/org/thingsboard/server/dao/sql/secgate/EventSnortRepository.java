package org.thingsboard.server.dao.sql.secgate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.thingsboard.server.dao.CountSig;
import org.thingsboard.server.dao.model.sql.EventSnortEntity;

import java.util.List;
import java.util.Map;

public interface EventSnortRepository extends JpaRepository<EventSnortEntity, Integer> {

/*    @Query("select count(event.signature) as num,sid,signature from event where sid = :sid GROUP BY signature,sid ORDER BY num DESC")
    List<Object> findBySidAndSignature(@Param("sid") Integer sid);*/

    @Query(value = "select count(event.signature) as num from event where sid = ?1 GROUP BY signature ", nativeQuery = true)
    List<Object> getSigntureCount(Integer sid);

    @Query(value = "select new org.thingsboard.server.dao.CountSig(count(e.signature),e.signature) from EventSnortEntity as e where e.sid =:sid GROUP BY e.signature ")
    List<CountSig> getSigntureCount2(@Param("sid") Integer sid);

//    where r.roomTypeId =:roomId

    @Query(value = "SELECT count(event.cid) FROM event WHERE sid = ?1 ",nativeQuery = true)
    Integer getAlertCount(Integer sid);
}
