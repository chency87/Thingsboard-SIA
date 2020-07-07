package org.thingsboard.server.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IDSConsole {

    //统计名称  / 设备名称
    private String name;

    //数量  /  signture 数量
    private Integer num;

    //占比  /  报警个数
    private Integer proportion;
}
