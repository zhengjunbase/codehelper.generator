package com.ccnode.codegenerator.server.service;

import com.ccnode.codegenerator.util.HttpUtil;
import com.ccnode.codegenerator.util.LoggerWrapper;
import org.junit.Test;
import org.slf4j.Logger;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/07/16 21:15
 */
public class PostServiceTest {

    private final static Logger LOGGER = LoggerWrapper.getLogger(PostServiceTest.class);

    @Test
    public void testLoop(){
        for (int i = 0; i < 100; i++) {
            try{
                test();

            }catch(Exception e){
                LOGGER.error("error",e);
            }
        }
    }

    @Test
    public void test(){

        String url = "http://115.28.149.106:8089/test/calculate";
        String json = "{\"data\":\"{\\\"queryId\\\":\\\"79e29cc1-b0ec-4ba3-8956-a1878703f82d\\\",\\\"carNo\\\":\\\"京QFK900\\\",\\\"vin\\\":\\\"LSVFL46R8D2010894\\\",\\\"engineNo\\\":\\\"B89929\\\",\\\"modelCode\\\":\\\"SVW71411NR\\\",\\\"remark\\\":\\\"手动档 风尚版 京5国Ⅳ\\\",\\\"seatNum\\\":5,\\\"registerDate\\\":\\\"2013-02-04 00:00:00\\\",\\\"registerAddress\\\":\\\"110100\\\",\\\"registerProvinceAddress\\\":\\\"110000\\\",\\\"carPrice\\\":70900.00,\\\"insAddress\\\":\\\"110100\\\",\\\"insProvinceAddress\\\":\\\"110000\\\",\\\"useProperty\\\":0,\\\"quoteInfoMap\\\":{\\\"zgpa\\\":[{\\\"riskCode\\\":\\\"QB205\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB204\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB203\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB202\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB201\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB104\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB103\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB102\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB101\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB100\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB001\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB000\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QF000\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB105\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB106\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB107\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB108\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB210\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB211\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB111\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB208\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB209\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB212\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB109\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB110\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB112\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB113\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB114\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB206\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB207\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB213\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB117\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null},{\\\"riskCode\\\":\\\"QB214\\\",\\\"riskName\\\":null,\\\"riskType\\\":null,\\\"coverage\\\":100000,\\\"insAmount\\\":null,\\\"premium\\\":null}]},\\\"standardName\\\":\\\"上海大众\\\",\\\"vehicleId\\\":\\\"4028b2883b6a6e18013b730e56f3074a\\\",\\\"parentId\\\":\\\"4028b2883b6a6e18013b749399a50a1d\\\",\\\"ownerInfo\\\":{\\\"ownerSex\\\":0,\\\"ownerName\\\":\\\"任士红\\\",\\\"ownerBirth\\\":null},\\\"domestic\\\":true}\"}";

        long startTime = System.currentTimeMillis();
        String s = HttpUtil.postJson(url, json);
        System.out.println(System.currentTimeMillis()-startTime);
        System.out.println(s);
    }

}