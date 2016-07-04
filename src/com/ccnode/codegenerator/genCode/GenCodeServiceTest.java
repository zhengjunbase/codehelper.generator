package com.ccnode.codegenerator.genCode;

import com.ccnode.codegenerator.pojo.GenCodeRequest;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/25 21:24
 */
public class GenCodeServiceTest {
    @Test
    public void genCode() throws Exception {


        List<String> list = Lists.newArrayList("TestPojo","TestPojoField");
        GenCodeRequest request = new GenCodeRequest(list,"/Users/zhengjun/Workspaces/genCodeSpace/MybatisGenerator","/");
        GenCodeService.genCode(request);
    }

}