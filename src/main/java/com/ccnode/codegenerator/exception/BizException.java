package com.ccnode.codegenerator.exception;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/22 16:31
 */
public class BizException extends RuntimeException {

    public BizException(){
    }

    public BizException(String s){
        super(s);
    }

}
