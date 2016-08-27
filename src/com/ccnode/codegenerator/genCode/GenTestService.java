package com.ccnode.codegenerator.genCode;

import com.ccnode.codegenerator.pojo.GenCodeResponse;
import com.ccnode.codegenerator.pojo.OnePojoInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/08/21 15:54
 */
public class GenTestService {

    private final static Logger LOGGER = LoggerFactory.getLogger(GenTestService.class);

    public static void genTest(GenCodeResponse response) {
        for (OnePojoInfo onePojoInfo : response.getPojoInfos()) {
            try {
                genTestFile(onePojoInfo, response);
            } catch (Exception e) {
                LOGGER.error("GenSqlService genSQL error", e);
            }
        }
    }

    private static void genTestFile(OnePojoInfo onePojoInfo, GenCodeResponse response) {


    }
}
