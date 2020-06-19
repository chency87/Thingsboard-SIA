package org.thingsboard.server.bean;

import lombok.Data;

import java.util.Date;

@Data
public class ResourceDetail {
    //资源名称
    private String name;

    //显示名称
    private String showName;

    //IP地址
    private String ip;

    //操作系统
    private String os;

    //可用状态
    private boolean status;

    //责任人
    private String person;

    //当前繁忙度
    private String busyness;

    //可用性
    private boolean usability;

    //资源类型
    private String type;

    //地理位置
    private String location;

    //健康度
    private String health;

    //运行时长
    private String duration;

    //最近采集时间
    private Date date;

    //描述
    private String desc;
}
