package org.thingsboard.server.casbin;

import org.casbin.jcasbin.main.Enforcer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.thingsboard.server.controller.AbstractControllerTest;
import org.thingsboard.server.plugin.bean.BaseIdentityAttr;
import org.thingsboard.server.plugin.bean.DynamicClassReflectUtils;
import org.thingsboard.server.service.security.permission.AccessPolicy;
import org.thingsboard.server.service.security.permission.EnforcerFactory;

import java.util.HashMap;
import java.util.Map;

public class JCasbinTest extends AbstractControllerTest {
//    @Qualifier("EnforcerFactory")
    @Autowired
    EnforcerFactory enforcerFactory;
    @Test
    public void test1222() {
        Enforcer enforcer = enforcerFactory.getEnforcer();
        AccessPolicy policy = new AccessPolicy("r.sub.age < 60","/data","READ");

        boolean addPolicy = enforcer.addPolicy("r.sub.age < 60","/data","READ");
        enforcer.addPolicy("r.sub.name == \"alice\"","/data","READ");


//        boolean addPolicy2 = enforcer.addPolicy("r.sub.age < 60","/data","READ");
//        boolean addPolicy = enforcer.addPolicy("");

        Map<String, Object> addProperties = new HashMap<>();
        addProperties.put("age", 18);
        Object obj = DynamicClassReflectUtils.getTarget(new BaseIdentityAttr(),addProperties);
        System.out.println(enforcer.enforce(obj, "/data","READ"));

        Map<String, Object> addProperties2 = new HashMap<>();
        addProperties2.put("name", "alice");
        Object obj2 = DynamicClassReflectUtils.getTarget(new BaseIdentityAttr(),addProperties2);
        System.out.println(enforcer.enforce(obj2, "/data","READ"));



        System.out.println(addPolicy);
        System.out.println(enforcer.getPolicy());
    }

    /**
     * 测试动态生成相关类
     *
     */
    @Test
    public void testDynamicBean(){
        Map<String, Object> addProperties = new HashMap<>();
        addProperties.put("day31", "你好");
        Object obj = DynamicClassReflectUtils.getTarget(new BaseIdentityAttr(),addProperties);
        System.out.println(obj + "**************************---------------");

    }

}
