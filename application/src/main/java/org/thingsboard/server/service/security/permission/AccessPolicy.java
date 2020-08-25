package org.thingsboard.server.service.security.permission;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.beans.Expression;

@Data
@AllArgsConstructor
public class AccessPolicy {

    //属性表达式
    private String sub;

    /**
     *  {objType,objId} 共同标识被访问对象
     */
    // 访问对象类型
    private String type;

    //访问对象对应id
    private String id;

    //操作
    private String act;

}
