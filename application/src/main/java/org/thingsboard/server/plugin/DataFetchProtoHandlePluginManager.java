package org.thingsboard.server.plugin;


import jnr.constants.Constant;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.thingsboard.server.controller.idscontroller.ConstantConfValue;
import org.thingsboard.server.plugin.bean.DataFetchPlugin;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Service
public class DataFetchProtoHandlePluginManager {

    private static final Logger logger = LoggerFactory.getLogger(DataFetchProtoHandlePluginManager.class);
    private List<DataFetchPlugin> pluginList;
    private SAXReader saxReader;

    private String xmlPluginFolder;

    public DataFetchProtoHandlePluginManager() {

        this.xmlPluginFolder = System.getProperty("user.dir")+ ConstantConfValue.xmlPluginFolder;
        this.xmlPluginFolder.replaceAll("//","/");
        File file = new File(this.xmlPluginFolder);
//        System.out.println(this.xmlPluginFolder + "/plugin.xml");
//        System.out.println("-***-*-******************");
        if(!file.exists())
        {
//            File f = new File(System.getProperty("user.dir")+ ConstantConfValue.xmlPluginFolder.substring(0,ConstantConfValue.xmlPluginFolder.lastIndexOf("/")));
            File f = new File(this.xmlPluginFolder);
            f.mkdirs();
            Document doc = DocumentHelper.createDocument();
            doc.addElement("plugins");
            write2Xml(doc);
        }

        pluginList = new ArrayList<>();
        saxReader =new SAXReader();
        try {
            pluginList = updatePluginList();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    //TODO: 读取插件配置文件plugin.xml中的内容
    public List<DataFetchPlugin> updatePluginList() throws DocumentException, MalformedURLException {
        pluginList.clear();
        Document document = saxReader.read(new File(this.xmlPluginFolder + "/plugin.xml"));
        //获取根目录
        Element root = document.getRootElement();
        List<?> plugins = root.elements("plugin");
        for(Object pluginObj : plugins) {
            Element pluginEle = (Element)pluginObj;
            DataFetchPlugin plugin = new DataFetchPlugin();
            plugin.setName(pluginEle.elementText("name"));
            plugin.setJar(pluginEle.elementText("jar"));
            plugin.setClassName(pluginEle.elementText("class"));
            plugin.setStatus((Boolean.valueOf(pluginEle.elementText("status"))));
            List<?> requires = pluginEle.element("requires").elements();
            List<String> tmpRequire = new ArrayList<>();
            for(Object reqObj : requires) {
                Element reqEle = (Element)reqObj;
                    tmpRequire.add(reqEle.getText());
            }
            plugin.setRequires(tmpRequire);
            pluginList.add(plugin);
        }
        return pluginList;
    }
    public DataFetchPlugin getPluginInfoFromList(String pluginName){

        try {
            pluginList = updatePluginList();
            for(DataFetchPlugin plugin : pluginList) {
                if(plugin.getName().equals(pluginName))
                {
                    return plugin;
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<DataFetchPlugin> getPluginList(){
        try {
            return  updatePluginList();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean hasPlugin(String pluginName){
        for(DataFetchPlugin plugin : pluginList) {
            if(plugin.getName().equals(pluginName))
            {
                return true;
            }
        }
        return false;
    }

     //TODO: 在plugin中写入/更新配置信息
    public Boolean addProto(DataFetchPlugin pluginInfo)  {

        boolean sucess = false;
    /*    if (pluginInfo==null || pluginInfo.getName().isEmpty() || pluginInfo.getStatus().toString().isEmpty() || pluginInfo.getClassName().isEmpty()){
            return false;
        }*/
        //1.创建文档
        Document doc= DocumentHelper.createDocument();
        Element plugins = doc.addElement("plugins");
        try {
            List<DataFetchPlugin> dataFetchPlugins = this.updatePluginList();
            if(dataFetchPlugins != null){
                for (DataFetchPlugin dataFetchPlugin:dataFetchPlugins){
                  boolean hasPlugin = dataFetchPlugin.getName().equals(pluginInfo.getName());
                  if (hasPlugin){
                        dataFetchPlugin.setJar(pluginInfo.getJar());
                        dataFetchPlugin.setClassName(pluginInfo.getClassName());
                        dataFetchPlugin.setStatus(pluginInfo.getStatus());
                        dataFetchPlugin.setRequires(pluginInfo.getRequires());
                    }
                    addElement(plugins, dataFetchPlugin);
                }
            }
            //添加节点及属性
            addElement(plugins, pluginInfo);
            return write2Xml(doc);
        } catch (IOException e) {
            sucess = false;
            e.printStackTrace();
        } catch (DocumentException e) {
            sucess = false;
            e.printStackTrace();
        }
        return sucess;
    }

    //TODO: 在plugin中删除插件信息
    public Boolean deleteProto(String name) throws MalformedURLException, DocumentException {
        Document doc=DocumentHelper.createDocument();
        Element plugins = doc.addElement("plugins");

            List<DataFetchPlugin> dataFetchPlugins = this.updatePluginList();
            if (dataFetchPlugins != null){
                for (DataFetchPlugin dataFetchPlugin:dataFetchPlugins){
                    if (dataFetchPlugin.getName().equals(name)){
                        dataFetchPlugins.remove(dataFetchPlugin);
                    }
                    addElement(plugins, dataFetchPlugin);
                }
            }
           return write2Xml(doc);
    }

    /**
     *  将写好内容的doc生成xml文件
     * @param doc
     * @return
     */
    private boolean write2Xml(Document doc){
        boolean status = true;
        FileOutputStream out = null;

        try {
//            out = new FileOutputStream(this.xmlPluginFolder);
           out = new FileOutputStream(System.getProperty("user.dir") + ConstantConfValue.xmlPluginFolder + "/plugin.xml");
            OutputFormat format= OutputFormat.createPrettyPrint();   //漂亮格式：有空格换行
            format.setEncoding("UTF-8");
            //1.创建写出对象
            XMLWriter writer=new XMLWriter(out,format);
            //2.写出Document对象
            writer.write(doc);
            //3.关闭流
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            status = false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            status = false;
        } catch (IOException e) {
            e.printStackTrace();
            status = false;
        }
        return status;
    }

    /**
     *  添加xml文件的具体内容
     * @param plugins
     * @param dataFetchPlugin
     */
    private void addElement(Element plugins, DataFetchPlugin dataFetchPlugin) {
        Element plugin = plugins.addElement("plugin");
        Element name = plugin.addElement("name");
        Element jar = plugin.addElement("jar");
        Element aClass = plugin.addElement("class");
        Element status = plugin.addElement("status");
        name.addText(dataFetchPlugin.getName());
        jar.addText(dataFetchPlugin.getJar());
        aClass.addText(dataFetchPlugin.getClassName());
        status.addText(dataFetchPlugin.getStatus().toString());

        Element requires1 = plugin.addElement("requires");
        for (int size = 0; size < dataFetchPlugin.getRequires().size(); size++) {
            Element require = requires1.addElement("require");
            require.addAttribute("name", "require" + size);
            require.addText(dataFetchPlugin.getRequires().get(size));
        }
    }
}
