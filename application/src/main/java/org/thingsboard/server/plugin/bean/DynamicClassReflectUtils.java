package org.thingsboard.server.plugin.bean;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class DynamicClassReflectUtils {

//    public static Object getTarget(Object dest, Map<String, Object> addProperties){
    public static Object getTarget(Object dest,Map<String, Object> addProperties){

        PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
        PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(dest);
        Map<String, Class> propertyMap = new HashMap<>();
        for (PropertyDescriptor d : descriptors) {
            if (!"class".equalsIgnoreCase(d.getName())) {
                propertyMap.put(d.getName(), d.getPropertyType());
            }
        }
        // add extra properties
        addProperties.forEach((k, v) -> propertyMap.put(k, v.getClass()));
        // new dynamic bean
        DynamicAttributeBean dynamicBean = new DynamicAttributeBean(propertyMap);

        // add old value
        propertyMap.forEach((k, v) -> {
            try {
                // filter extra properties
                if (!addProperties.containsKey(k)) {
                    dynamicBean.setValue(k, propertyUtilsBean.getNestedProperty(dest, k));
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
        // add extra value
        addProperties.forEach((k, v) -> {
            try {
                dynamicBean.setValue(k, v);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
        Object target = dynamicBean.getTarget();
        return target;
    }
}
