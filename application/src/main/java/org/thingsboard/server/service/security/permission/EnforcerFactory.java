package org.thingsboard.server.service.security.permission;

import org.casbin.adapter.JDBCAdapter;
import org.casbin.jcasbin.main.Enforcer;
import org.casbin.jcasbin.model.Model;
import org.casbin.jcasbin.persist.Adapter;
import org.casbin.jcasbin.persist.file_adapter.FileAdapter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.thingsboard.server.config.EnforcerConfigProperties;

import static org.casbin.jcasbin.main.CoreEnforcer.newModel;

//(value = "EnforcerFactory")
@Component
public class EnforcerFactory implements InitializingBean {
    private ABACEnforcer enforcer;
    @Autowired
    private EnforcerConfigProperties config;

    @Override
    public void afterPropertiesSet() throws Exception {
        //从数据库读取策略
        JDBCAdapter jdbcAdapter = new JDBCAdapter(config.getDriverClassName(),config.getUrl(),config.getUsername(),
                config.getPassword());

        Model m = newModel();
        m.addDef("r", "r", "sub, type, obj, act");
        m.addDef("p", "p", "sub_rule, type, obj, act");
        m.addDef("e", "e", "some(where (p.eft == allow))");
        m.addDef("m", "m", "eval(p.sub_rule) && r.obj == p.obj&& r.type == p.type && r.act == p.act");


//        Enforcer e = new Enforcer(m, a);


        enforcer = new ABACEnforcer(m, jdbcAdapter);
//        enforcer = new ABACEnforcer(m, jdbcAdapter);
        enforcer.loadPolicy();//Load the policy from DB

    }

    public ABACEnforcer getEnforcer(){
        return enforcer;
    }

}
