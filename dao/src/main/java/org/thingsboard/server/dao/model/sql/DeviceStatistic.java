package org.thingsboard.server.dao.model.sql;

public interface DeviceStatistic {

	Object getIPsrc();
	Integer getNum();
	default String toStringInfo() {
		return "ip_src=" + getIPsrc().toString() + "; Num=" + getNum(); 
	}
}
