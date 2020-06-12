package org.thingsboard.server.plugin;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Service;
import org.thingsboard.server.plugin.bean.DataFetchPlugin;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataFetchPluginManager {
    private URLClassLoader urlClassLoader;
    private List<DataFetchPlugin> pluginList;
    private SAXReader saxReader;

    final static String xmlPluginFolder = "\\application\\conf\\plugin.xml";
    public DataFetchPluginManager() throws DocumentException, MalformedURLException {
        pluginList = new ArrayList<>();
        saxReader =new SAXReader();
        //增加代码，若conf.xml文件不存在，则创建一个文件

//        File file=new File(System.getProperty("user.dir")+xmlPluginFolder);
//        if(!file.exists())
//        {
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        pluginList = updatePluginList();

    }

    //TODO: 读取插件配置文件plugin.xml中的内容
    public List<DataFetchPlugin> updatePluginList() throws DocumentException, MalformedURLException {
        pluginList.clear();
//        SAXReader saxReader =new SAXReader();
        Document document = saxReader.read(new File(System.getProperty("user.dir")+xmlPluginFolder));
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
        init(pluginList);
        return pluginList;
    }


    private void init(List<DataFetchPlugin> plugins) throws MalformedURLException {
        int size = plugins.size();
        URL[] urls = new URL[size];


        for(int i = 0; i < size; i++) {
            DataFetchPlugin plugin = plugins.get(i);
            if(plugin.getStatus()){
                String filePath = plugin.getJar();
                urls[i] = new URL("file:" + filePath);
            }
        }
        // 将jar文件组成数组，来创建一个URLClassLoader
        urlClassLoader = new URLClassLoader(urls);
    }

    public AsyncDataFetchPluginService getInstanceByClassName(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        // 插件实例化对象，插件都是实现PluginService接口
        Class<?> clazz = urlClassLoader.loadClass(className);
        Object instance = clazz.newInstance();
        return (AsyncDataFetchPluginService)instance;
    }
    public AsyncDataFetchPluginService getInstanceByPluginName(String pluginName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> clazz = null;
        for(DataFetchPlugin plugin : pluginList) {
            if(plugin.getName().equals(pluginName))
            {
                clazz = urlClassLoader.loadClass(plugin.getClassName());
            }
        }
        if(clazz != null)
        {
            return (AsyncDataFetchPluginService)clazz.newInstance();

        }else {
            return null;
        }
    }
    public DataFetchPlugin getPlginInfoFromList(String pluginName){
        for(DataFetchPlugin plugin : pluginList) {
            if(plugin.getName().equals(pluginName))
            {
                return plugin;
            }
        }
        return null;
    }
    public List<DataFetchPlugin> getPluginList(){
        return  this.pluginList;
    }

    //TODO: 在plugin中写入配置信息
    public Boolean addProto(){

        return false;
    }



    //TODO: 在plugin中写入/更新配置信息
    public Boolean addProto(DataFetchPlugin pluginInfo)  {

        if (pluginInfo==null || pluginInfo.getName().isEmpty() || pluginInfo.getStatus().toString().isEmpty() || pluginInfo.getClassName().isEmpty()){
            return false;
        }

        Boolean sucess = null;
        FileOutputStream out = null;
        //1.创建文档
        Document doc= DocumentHelper.createDocument();
        Element plugins = doc.addElement("plugins");
        try {
            List<DataFetchPlugin> dataFetchPlugins = this.updatePluginList();
            // dataFetchPlugins.add(pluginInfo);

            for (DataFetchPlugin dataFetchPlugin:dataFetchPlugins){

                if (dataFetchPlugin.getName().equals(pluginInfo.getName())){
                    dataFetchPlugin.setJar(pluginInfo.getJar());
                    dataFetchPlugin.setClassName(pluginInfo.getClassName());
                    dataFetchPlugin.setStatus(pluginInfo.getStatus());
                    dataFetchPlugin.setRequires(pluginInfo.getRequires());
                }
                //添加节点及属性
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
                for (int size= 0; size<dataFetchPlugin.getRequires().size(); size++){
                    Element require = requires1.addElement("require");
                    require.addAttribute("name", "require"+size);
                    require.addText(dataFetchPlugin.getRequires().get(size));
                }
            }

            //指定文件输出的位置
            out = new FileOutputStream(System.getProperty("user.dir")+xmlPluginFolder);
            // 指定文本的写出的格式：
            OutputFormat format= OutputFormat.createPrettyPrint();   //漂亮格式：有空格换行
            format.setEncoding("UTF-8");
            //1.创建写出对象
            XMLWriter writer=new XMLWriter(out,format);
            //2.写出Document对象
            writer.write(doc);
            //3.关闭流
            writer.close();

            sucess= true;
        } catch (FileNotFoundException e) {
            sucess = false;
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            sucess = false;
            e.printStackTrace();
        } catch (IOException e) {
            sucess = false;
            e.printStackTrace();
        } catch (DocumentException e) {
            sucess = false;
            e.printStackTrace();
        }
        return sucess;
    }


    //TODO: 在plugin中删除插件
    public Boolean deleteProto(DataFetchPlugin pluginInfo)  {

        if (pluginInfo==null || pluginInfo.getName().isEmpty()){
            return false;
        }

        Boolean sucess = null;
        FileOutputStream out = null;
        //1.创建文档
        Document doc=DocumentHelper.createDocument();
        Element plugins = doc.addElement("plugins");
        try {
            List<DataFetchPlugin> dataFetchPlugins = this.updatePluginList();

            for (DataFetchPlugin dataFetchPlugin:dataFetchPlugins){
                if (dataFetchPlugin.getName().equals(pluginInfo.getName())){
                    dataFetchPlugins.remove(dataFetchPlugin);
                }else {
                    return false;
                }
                //添加节点及属性
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
                for (int size= 0; size<dataFetchPlugin.getRequires().size(); size++){
                    Element require = requires1.addElement("require");
                    require.addAttribute("name", "require"+size);
                    require.addText(dataFetchPlugin.getRequires().get(size));
                }
            }
            //指定文件输出的位置
            out = new FileOutputStream(System.getProperty("user.dir")+xmlPluginFolder);
            // 指定文本的写出的格式：
            OutputFormat format= OutputFormat.createPrettyPrint();   //漂亮格式：有空格换行
            format.setEncoding("UTF-8");
            //1.创建写出对象
            XMLWriter writer=new XMLWriter(out,format);
            //2.写出Document对象
            writer.write(doc);
            //3.关闭流
            writer.close();

            sucess= true;
        } catch (FileNotFoundException e) {
            sucess = false;
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            sucess = false;
            e.printStackTrace();
        } catch (IOException e) {
            sucess = false;
            e.printStackTrace();
        } catch (DocumentException e) {
            sucess = false;
            e.printStackTrace();
        }
        return sucess;
    }



}
