package org.thingsboard.server.plugin.bean;

import lombok.Data;

import java.util.Map;

/**
 *  HMI页面属性信息实体
 */
@Data
public class HMIArr {

    private String token;
    private String ID;
    private Boolean status;
    private Map<String,String> arr;
}
