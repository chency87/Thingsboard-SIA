
package org.thingsboard.server.dao.model.sql;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(value = DoubleKeys.class)
public class Dashboard1 {

	
	@Id
	@Column(name="cid")
	private Integer cid;
	@Id
	@Column(name="sid")
	private Integer sid;
	
	private long ipSrc;
}
