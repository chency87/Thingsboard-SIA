package org.thingsboard.server.bean;

import lombok.Data;

@Data
public class DisplayCard {
	Integer numOfDevice;
	Integer numOfAbnormal;
	Integer numOfProtocol;
	double numOfDataCache;
	
}
