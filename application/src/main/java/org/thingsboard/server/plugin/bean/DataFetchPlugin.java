package org.thingsboard.server.plugin.bean;

import lombok.Data;

import java.util.List;

@Data
public class DataFetchPlugin {
    //插件名称，协议名
    private String name;
    //插件jar包位置
    private String jar;
    //插件类名称
    private String className;
    //插件是否被启用
    private Boolean status = true;
    //插件所需要的参数名称
    private List<String> requires;
}
