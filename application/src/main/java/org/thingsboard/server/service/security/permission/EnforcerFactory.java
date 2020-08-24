package org.thingsboard.server.service.security.permission;

import org.casbin.adapter.JDBCAdapter;
import org.casbin.jcasbin.main.Enforcer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.thingsboard.server.config.EnforcerConfigProperties;


@Component(value = "EnforcerFactory")
public class EnforcerFactory implements InitializingBean {

    private Enforcer enforcer;

    @Autowired
    private EnforcerConfigProperties config;
//    enforcerConfigProperties;

//    private EnforcerConfigProperties config;


    @Override
    public void afterPropertiesSet() throws Exception {
//        config = enforcerConfigProperties;
        //从数据库读取策略
//        JDBCAdapter
        JDBCAdapter jdbcAdapter = new JDBCAdapter(config.getDriverClassName(),config.getUrl(),config.getUsername(),
                config.getPassword());
        enforcer = new Enforcer(config.getModelPath(), jdbcAdapter);
        enforcer.loadPolicy();//Load the policy from DB

    }

    public Enforcer getEnforcer(){
        return enforcer;
    }

}
