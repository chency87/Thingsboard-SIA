package org.thingsboard.server.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemParameter {
    private String systemName;
    private String systemVersion;
    private Long runningTime;
    private Double cpuUtilization;
    private String memoryUtilization;
    private String swapUtilization;
    private String storageSpaceUtilization;
    Integer numOfDevice;//
//    Integer numOfAbnormal;//异常数
    Integer numOfProtocol;//协议数
    double numOfDataCache;//缓存数据
}
