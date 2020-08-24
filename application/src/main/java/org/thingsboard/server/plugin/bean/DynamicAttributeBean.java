package org.thingsboard.server.plugin.bean;

import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DynamicAttributeBean {
    private Object target;
    /**
     * 属性集合
     */
    private BeanMap beanMap;

    public DynamicAttributeBean(Map<String, Class> propertyMap){
        this.target = generateBean(propertyMap);
        this.beanMap = BeanMap.create(this.target);
    }

    public void setValue(String property, Object value) {
        beanMap.put(property, value);
    }

    public Object getValue(String property) {
        return beanMap.get(property);
    }

    public Object getTarget() {
        return this.target;
    }

    private Object generateBean(Map<String, Class> propertyMap) {
        BeanGenerator generator = new BeanGenerator();
        Set keySet = propertyMap.keySet();
        for(Iterator i = keySet.iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            generator.addProperty(key, (Class) propertyMap.get(key));
        }
        return generator.create();
    }

}
