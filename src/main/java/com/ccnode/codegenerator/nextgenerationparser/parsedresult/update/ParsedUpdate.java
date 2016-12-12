package com.ccnode.codegenerator.nextgenerationparser.parsedresult.update;

import com.ccnode.codegenerator.nextgenerationparser.parsedresult.QueryRule;
import com.rits.cloning.Cloner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class ParsedUpdate {
    private List<String> updateProps;

    private List<QueryRule> queryRules;


    public void addUpdateProps(String prop){
        if(updateProps==null){
            updateProps = new ArrayList<>();
        }
        updateProps.add(prop);
    }

    public ParsedUpdate clone() {
        return Cloner.standard().deepClone(this);
    }


    public void addQueryProp(String queryProp) {
        if (this.queryRules == null) {
            this.queryRules = new ArrayList<>();
        }
        QueryRule rule = new QueryRule();
        rule.setProp(queryProp);
        this.queryRules.add(rule);
    }

    public void addQueryOperator(String operator) {
        QueryRule rule = this.queryRules.get(this.queryRules.size() - 1);
        rule.setOperator(operator);
    }

    public void addConnector(String connector) {
        QueryRule rule = this.queryRules.get(this.queryRules.size() - 1);
        rule.setConnector(connector);
    }
}
