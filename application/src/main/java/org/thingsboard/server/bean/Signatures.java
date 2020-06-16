package org.thingsboard.server.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Signatures {

    //优先级
    private Integer prio;

    //签名
    private String signture;

    //传感器
    private Integer sensors;

    //报警数量
    private Integer alerts;

    //源
    private Integer srcs;

    //目的
    private Integer dests;
}
