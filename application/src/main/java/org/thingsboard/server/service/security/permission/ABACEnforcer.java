package org.thingsboard.server.service.security.permission;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.common.protocol.types.Field;
import org.casbin.jcasbin.main.Enforcer;
import org.casbin.jcasbin.model.FunctionMap;
import org.casbin.jcasbin.model.Model;
import org.casbin.jcasbin.persist.Adapter;
import org.casbin.jcasbin.persist.file_adapter.FileAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class ABACEnforcer extends Enforcer {

//    @Autowired
//    private ExpressionParser parser;

    public ABACEnforcer() {
        super("","");
    }

    public ABACEnforcer(String modelPath, String policyFile) {
        super(modelPath,policyFile);
    }

    public ABACEnforcer(String modelPath, Adapter adapter) {
       super(modelPath,adapter);
    }

    public ABACEnforcer(Model m, Adapter adapter) {
        super(m,adapter);
    }

    public ABACEnforcer(Model m) {
        super(m);
    }

    public ABACEnforcer(String modelPath) {
        super(modelPath);
    }

    public ABACEnforcer(String modelPath, String policyFile, boolean enableLog) {
       super(modelPath,policyFile,enableLog);
    }

    public boolean enforcer(Object sub, Object type,Object id,Object act){
        SecurityAccessContext cxt = new SecurityAccessContext(sub,type,id,act,null);
        List<List<String>> policy = getPolicy();
        List<List<String>> filteredRules = filterRules(policy,cxt);
        return checkAccessRules(filteredRules,cxt);
    }

    @Override
    public List<String> getAllActions() {
        return this.getAllNamedActions("p");
    }

    @Override
    public List<String> getAllNamedActions(String ptype) {
        return getModel().getValuesForFieldInPolicy("p",ptype,3);
    }

    public List<String> getAllNamedAccessTarget(){
        return null;
    }

    private List<List<String>> filterRules(List<List<String>> allRules, SecurityAccessContext cxt){
        return allRules.parallelStream().filter( item ->
            {
                if(cxt.getAct().toString().equals(Operation.CREATE.toString())
                        && isNotEmptyAndBlank(cxt.getType().toString())){

                    if( cxt.getType().toString().equals(item.get(1))
                            && cxt.getAct().toString().equals(item.get(3))){
                        return true;
                    }
                }else{
                    if(isNotEmptyAndBlank(cxt.getType().toString())
                            && isNotEmptyAndBlank(cxt.getId().toString())
                            && cxt.getType().toString().equals(item.get(1))
                            && cxt.getId().toString().equals(item.get(2))){
                        if(item.get(3).equals(Operation.ALL.toString()) || cxt.getAct().equals(item.get(3))){
                            return true;
                        }
                    }
                }
                return false;
            }).collect(Collectors.toList());
    }

    private boolean checkAccessRules(List<List<String>> matchedRules, SecurityAccessContext cxt) {
        ExpressionParser parser = new SpelExpressionParser();
        return matchedRules.parallelStream().anyMatch(item -> {try{
            if(parser.parseExpression(item.get(0)).getValue(cxt,Boolean.class))
                return true;
        }catch (EvaluationException ex){
//            log.error("An error occurred while evaluating PolicyRule.", ex);
        }
            return false;
        });
    }
    private boolean isNotEmptyAndBlank(String str){ return StringUtils.isNotBlank(str) && StringUtils.isNotEmpty(str);}
}
