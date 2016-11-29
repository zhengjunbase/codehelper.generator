package com.ccnode.codegenerator.pojoHelper;

import com.ccnode.codegenerator.util.LoggerWrapper;
import org.junit.Test;
import org.slf4j.Logger;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/07/16 22:07
 */
public class ServerRequestHelperTest {

    private final static Logger LOGGER = LoggerWrapper.getLogger(ServerRequestHelperTest.class);


    @Test
    public void testgetMacLoop(){
        for (int i = 0; i < 100; i++) {
            try{
                testgetMac();

            }catch(Exception e){
                LOGGER.error("error",e);
            }
        }
    }

    @Test
    public void testgetMac(){
        System.out.println(ServerRequestHelper.getIpAddress());
    }

}