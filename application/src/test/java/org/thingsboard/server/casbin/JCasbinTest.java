package org.thingsboard.server.casbin;

import org.apache.kafka.common.protocol.types.Field;
import org.casbin.jcasbin.main.Enforcer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.thingsboard.server.common.data.EntityType;
import org.thingsboard.server.controller.AbstractControllerTest;
import org.thingsboard.server.plugin.bean.BaseIdentityAttr;
import org.thingsboard.server.plugin.bean.DynamicClassReflectUtils;
import org.thingsboard.server.service.security.permission.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JCasbinTest extends AbstractControllerTest {
//    @Qualifier("EnforcerFactory")
    @Autowired
    EnforcerFactory enforcerFactory;



    @Test
    public void test1222() {
        Enforcer enforcer = enforcerFactory.getEnforcer();
//        Enforcer enforcer = new Enforcer("conf/abac_rule_model.conf", "conf/abac_rule_policy.csv");
//        AccessPolicy policy = new AccessPolicy("r.sub.age < 60","/data","READ");

//        enforcer.addPolicy("r.sub.name == alice","DEVICE","/data","WRITE");
        boolean addPolicy = enforcer.addPolicy("r.sub.age > 18 && r.sub.age < 25","DEVICE","/data","READ");
//        Map<String, Object> addProperties = new HashMap<>();
//        addProperties.put("age", 18);
//        Object obj = DynamicClassReflectUtils.getTarget(new BaseIdentityAttr(),addProperties);
//        System.out.println(enforcer.enforce(obj, "/data","READ"));
//
//
//        TestEvalRule3 alice = new TestEvalRule3("alice");
//        System.out.println(enforcer.enforce(alice, "/data","WRITE"));

//        Map<String, Object> addProperties2 = new HashMap<>();
//        addProperties2.put("name", "alice");
//        Object obj2 = DynamicClassReflectUtils.getTarget(new BaseIdentityAttr(),addProperties2);
//        System.out.println(enforcer.enforce(obj2, "/data","WRITE"));
//        TestEvalRule2 alice = new TestEvalRule2(20);

        System.out.println(enforcer.getAllActions());
        System.out.println(addPolicy);
        System.out.println(enforcer.getAllObjects());
        System.out.println(enforcer.getAllNamedObjects("p"));


        System.out.println(enforcer.getPolicy());
    }

    @Test
    public void testABACEnforcer(){
//        Enforcer abacEnforcer = new ABACEnforcer("conf/abac_rule_model.conf", "conf/abac_rule_policy.csv");
        Enforcer abacEnforcer = enforcerFactory.getEnforcer();
        testAddPolicy(abacEnforcer);
        testDelPolicy(abacEnforcer);
        Map<String, Object> addProperties = new HashMap<>();
        addProperties.put("name", "");
        addProperties.put("age", 19);
        Object obj = DynamicClassReflectUtils.getTarget(new BaseIdentityAttr(),addProperties);
        System.out.println(((ABACEnforcer) abacEnforcer).enforce(obj,"DEVICE","1eabb6a85cacf508cc473c0690c1ed2","ASSIGN_TO_CUSTOMER"));

    }
    private void testDelPolicy(Enforcer enforcer){
        AccessPolicy ap = new AccessPolicy("sub.name == 'alice'", EntityType.DEVICE.toString(),"/data", Operation.DELETE.toString());
        enforcer.removePolicy(ap.getSub(),ap.getType(),ap.getId(),ap.getAct());
    }

    private void testAddPolicy(Enforcer enforcer){
        enforcer.addPolicy("sub.name == 'alice'", EntityType.DEVICE.toString(),"1eac4bd00b20070a0b419fb1d81e3da", Operation.DELETE.toString());
        enforcer.addPolicy("sub.name == 'alice'",EntityType.DEVICE.toString(),"1eac4bd00b20070a0b419fb1d81e3da",Operation.READ.toString());
        enforcer.addPolicy("sub.name == 'alice'",EntityType.DEVICE.toString(),"1eac4bd00b20070a0b419fb1d81e3da",Operation.WRITE.toString());
        enforcer.addPolicy("sub.name == 'alice'",EntityType.DEVICE.toString(),"1eac4bd00b20070a0b419fb1d81e3da",Operation.WRITE_TELEMETRY.toString());
        enforcer.addPolicy("sub.name == 'alice'",EntityType.DEVICE.toString(),"1eabb6a85cacf508cc473c0690c1ed2", Operation.DELETE.toString());
        enforcer.addPolicy("sub.name == 'alice'",EntityType.DEVICE.toString(),"1eabb6a85cacf508cc473c0690c1ed2",Operation.READ.toString());
        enforcer.addPolicy("sub.name == 'alice'",EntityType.DEVICE.toString(),"1eabb6a85cacf508cc473c0690c1ed2",Operation.WRITE.toString());
        enforcer.addPolicy("sub.name == 'alice' && sub.age > 18",EntityType.DEVICE.toString(),"1eabb6a85cacf508cc473c0690c1ed2",Operation.WRITE_TELEMETRY.toString());

        enforcer.addPolicy("sub.name == 'alice' || sub.age > 18",EntityType.DEVICE.toString(),"1eabb6a85cacf508cc473c0690c1ed2",Operation.ASSIGN_TO_CUSTOMER.toString());

        enforcer.addPolicy("sub.name == 'bob'",EntityType.DEVICE.toString(),"1eabb6a85cacf508cc473c0690c1ed2",Operation.ALL.toString());
    }

    @Test
    public void testEvalSPEL() {
        ExpressionParser parser = new SpelExpressionParser();
//        SecurityAccessContext asc = new SecurityAccessContext("alice","DEVICE","1999","READ",null);
//        EvaluationContext context = new StandardEvaluationContext();
//        context.setVariable("asx", asc);
        String accessObjType = "DEVICE";
        String accessObjId = "1998";
        String targetExpression = "objectType == '" +
                accessObjType +
                "' && objectId == '" +
                accessObjId +
                "'";
        String conditions = "name == 'alice'";
        try {
            Map<String, Object> addProperties = new HashMap<>();
            addProperties.put("name", "alice");
            Object obj = DynamicClassReflectUtils.getTarget(new BaseIdentityAttr(),addProperties);

            TestEvalRule3 rule3 = new TestEvalRule3(null);
            System.out.println(parser.parseExpression(conditions).getValue(rule3,Boolean.class));

        } catch(EvaluationException ex) {
            System.err.println("An error occurred while evaluating PolicyRule."+ ex);
        }
    }

//    @Test
    public void testEvalWithDomain() {
        Enforcer e = new Enforcer("conf/abac_rule_with_domains_model.conf", "conf/abac_rule_with_domains_policy.csv");
        System.out.println(e.enforce("alice", "domain1", "data1", "read"));

        System.out.println(e.getPolicy());
        System.out.println(e.getAllActions());

        System.out.println(e.getAllSubjects());
        System.out.println(e.getPermissionsForUser("alice"));
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

    public static class TestEvalRule2 {
        private int age;
        TestEvalRule2(int age){
            this.age=age;

        }
        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    public static class TestEvalRule3{
        private String name;
        TestEvalRule3(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class TestEvalRule { //This class must be static.
        private String name;
        private int age;

        TestEvalRule(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

}
