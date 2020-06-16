package org.thingsboard.server.dao.model.sql;
/**
 * 没用了
 */
import java.io.Serializable;

import javax.persistence.Column;

import lombok.Data;
@Data
public class DoubleKeys implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "sid")
	private Integer sid;
	
	@Column(name = "cid")
	private Integer cid;

}
