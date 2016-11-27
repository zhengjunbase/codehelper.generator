package com.ccnode.codegenerator.utils;

import com.ccnode.codegenerator.util.GenCodeUtil;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by bruce.ge on 2016/11/27.
 */
public class GenCodeUtilTest {
    @Test
    public void test(){
        GenCodeUtil.deducePackage("src/main/java/com/rest/mapper","com.rest.domain","D:\\code\\coding.net\\upworkattachment\\src\\main\\java\\com\\rest\\domain\\PeoplePO.java");
    }


    @Test
    public void doThings(){
        String a  = "D:\\code\\coding.net\\upworkattachment\\src\\main\\java\\com\\rest\\mapper";

        a  ="D:\\code\\coding.net\\upworkattachment\\src\\\\main\\\\java\\\\com\\\\rest\\\\mapper\\APODao.java";

        String b = "com.rest.domain";

        String c = "D:\\code\\coding.net\\upworkattachment\\src\\main\\java\\com\\rest\\domain\\PeoplePO.java";

        Path path = Paths.get(c);

        String[] split = b.split("\\.");
        int len = split.length;
        while(len>=0){
            path = path.getParent();
            len--;
        }



        //a 不为空的情况 可以这么弄

        System.out.println(path.toString());

        Path  mapper = Paths.get(a);
        mapper = mapper.getParent();
        //shall combine two path
        Path relativize = path.relativize(mapper);
        String relate = relativize.toString();

        String replace = relate.replace("\\", ".");

        System.out.println(replace);

    }
}
