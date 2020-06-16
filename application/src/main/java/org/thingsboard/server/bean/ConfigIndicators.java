package org.thingsboard.server.bean;

import lombok.Data;

/**
 *  配置指标
 */
@Data
public class ConfigIndicators {

    //IP地址
    private String ip;

    //主机名
    private String name;

    //CPU个数
    private Integer CPUNum;

    //内存总容量
    private Double MemoryCapacity;

    //硬盘个数
    private Integer HardDiskNum;

    //硬盘总容量
    private Double HardDiskCapacity;

    //主机说明
    private String hostDesc;

    //主机操作系统
    private String hostOS;

    //主机的所有IP地址
    private String allIP;

    //分区总容量
    private Double partitionCapacity;

    //Swap Space已用大小
    private Double swapSpace;



}
