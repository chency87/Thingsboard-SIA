
package org.thingsboard.server.dao.sql.secgate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import org.thingsboard.server.dao.model.sql.EventsEntity;

public interface EventsRepository extends JpaRepository<EventsEntity, Integer>  {
	@Query(value = "SELECT COUNT(DISTINCT(cid)) FROM event;", nativeQuery = true)
	int queryeventCount();
//	@Query(value = "select n.id,GROUP_CONCAT(e.ent_id) from news_info n INNER JOIN map_news_company e on e.news_id = n.id where n.pub_date>= ?1 and n.id>?2 group by n.id order by n.id limit 10000", nativeQuery = true)
//	List<Object[]> listBypubDateWithEnts(String pubDate, long news_id);
	@Query(value = "SELECT COUNT(DISTINCT(cid)) FROM event WHERE timestamp >= to_timestamp(?1,'yyyy-MM-dd hh24:mi:ss') AND timestamp < to_timestamp(?2,'yyyy-MM-dd hh24:mi:ss') ", nativeQuery = true)
	int getCountsBytime(String startTime, String endTime);
//	public List<EventsEntity> findAll(int page,int size);

}

