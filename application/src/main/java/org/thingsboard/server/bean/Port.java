package org.thingsboard.server.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Port {

    //端口号
    private Integer port;

    //数量
    private Integer nums;
}
