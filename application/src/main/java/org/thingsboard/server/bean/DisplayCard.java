package org.thingsboard.server.bean;

import lombok.Data;

@Data
public class DisplayCard {
	Integer numOfDevice;//IP数
	Integer numOfAbnormal;//异常数
	Integer numOfProtocol;//协议数
	double numOfDataCache;//缓存数据
	
}
