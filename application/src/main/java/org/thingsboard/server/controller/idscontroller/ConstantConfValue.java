package org.thingsboard.server.controller.idscontroller;

import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

public class ConstantConfValue {
    public final static String xmlPluginFolder="\\static\\proto\\plugin.xml";
    public final static String dataFetchJobGroupNameSuffix="DATAFETCH";

    public final static String dataFetchJobDataParamClassName="className";
    public final static String dataFetchJobDataParamJarPath="jarPath";
    public final static String dataFetchJobDataParamToken="token";

    public final static String protoHandlePluginUpload = "\\static\\proto";


    public final static String dataFetchPluginName="pluginName";


    public final static String dataTransportExecJsonAlias = "ExecuteJson";


    public final static String dataTransportJobName = "DATATRANSPORT_JOB";
    public final static String dataTransportJobGroupNameSuffix = "DATATRANSPORT_JOB";



//    public final static String pluginClassName="jobName";
//    public final static String pluginJarPath="jobName";
//    public final static String jobToken="jobName";

}
