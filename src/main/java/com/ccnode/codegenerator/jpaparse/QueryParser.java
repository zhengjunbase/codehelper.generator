package com.ccnode.codegenerator.jpaparse;


import com.ccnode.codegenerator.jpaparse.parser.DeleteParse;
import com.ccnode.codegenerator.jpaparse.parser.FindParser;
import com.ccnode.codegenerator.jpaparse.parser.UpdateParser;
import com.intellij.psi.xml.XmlTag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by bruce.ge on 2016/12/4.
 */
public class QueryParser {

    /**
     * @param methodName
     * @param props      the props of bean need to use with
     * @return
     */
    public static XmlTag parse(XmlTag rootTag, String orginMethodName, List<String> props, String tableName, String returnClassName) {
        String methodName = orginMethodName.toLowerCase();
        Collections.sort(props, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.length() - o1.length();
            }
        });
        if (methodName.startsWith(KeyWordConstants.FIND)) {
            //user find parser to parse.
            String sql = FindParser.parse(methodName, props, tableName);
            XmlTag select = rootTag.createChildTag("select", "", "\n"+sql+"\n", false);
            select.setAttribute("id", orginMethodName);
            select.setAttribute("resultType", returnClassName);
            return select;
        } else if (methodName.startsWith(KeyWordConstants.UPDATE)) {
            String sql = UpdateParser.parse(methodName, props, tableName);
            XmlTag update = rootTag.createChildTag("update", "", "\n"+sql+"\n", false);
            update.setAttribute("id", orginMethodName);
            return update;
        } else if (methodName.startsWith(KeyWordConstants.DELETE)) {
            String sql = DeleteParse.parse(methodName, props, tableName);
            XmlTag delete = rootTag.createChildTag("delete", "", "\n"+sql+"\n", false);
            delete.setAttribute("id", orginMethodName);
            return delete;
        }
        throw new ParseException("method name not start with find or update or delete");
    }


    //test more than expected.
    public static void main(String[] args) {
        //make it true;
        List<String> props = new ArrayList<String>();
        props.add("username");
        props.add("passworld");
//        Assertions.assertThat(parse("findByUserName", props, "user")).isEqualTo("select * from user where username = {0}");
//        Assertions.assertThat(parse("findByUserNameAndPassword", props, "user")).isEqualTo("select * from user where username = {0} and password = {1}");
//        Assertions.assertThat(parse("findIdByUserNameAndPassword", props, "user")).isEqualTo("select id from user where username = {0} and password = {1}");
//        Assertions.assertThat(parse("findByUserNameOrPassword", props, "user")).isEqualTo("select * from user where username = {0} or password = {1}");
//        Assertions.assertThat(parse("findByAgeBetween", props, "user")).isEqualTo("select * from user where age >= {0} and age <= {1}");
//        Assertions.assertThat(parse("findByUserNameLike", props, "user")).isEqualTo("select * from user where username like {0}");
//        Assertions.assertThat(parse("findByAgeLessThan", props, "user")).isEqualTo("select * from user where age <= {0}");
//        Assertions.assertThat(parse("findByAgeGreaterThan", props, "user")).isEqualTo("select * from user where age >={0}");
//        Assertions.assertThat(parse("findByUserNameIsNotNull", props, "user")).isEqualTo("select * from user where username is not null");
//        Assertions.assertThat(parse("findByUserNameIsNull", props, "user")).isEqualTo("select * from user where username is null");
//        Assertions.assertThat(parse("findByUserNameNot", props, "user")).isEqualTo("select * from user where username != {0}");
//        Assertions.assertThat(parse("findByAgeIn", props, "user")).isEqualTo("select * from user where username in {0}");
//        Assertions.assertThat(parse("findByUserNameOrderById", props, "user")).isEqualTo("select * from user where username = {0} order by id");
//        Assertions.assertThat(parse("findByUserNameOrderByIdDesc", props, "user")).isEqualTo("select * from user where username = {0} order by id desc");
//        Assertions.assertThat(parse("findIdAndUserNameByUserNameOrderByIdDesc", props, "user")).isEqualTo("select id,username from user where username = {0} order by id desc");
    }


}
