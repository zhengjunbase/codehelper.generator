package com.ccnode.codegenerator.pojo;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/04/13 10:26
 */
public enum RetStatus {

    SUCCESS(0,"成功"),
    FAILURE(1,"失败"),
    NONE(-1,"none");

    private Integer code;
    private String desc;

    private RetStatus(Integer code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static RetStatus fromName(String name){
        for(RetStatus e : RetStatus.values()){
            if (e.name().equalsIgnoreCase(name)){
                return e;
            }
        }
        return RetStatus.NONE;
    }

    public static RetStatus fromCode(Integer code){
        for(RetStatus e : RetStatus.values()){
            if (e.getCode() == code){
                return e;
            }
        }
        return RetStatus.NONE;
    }

    public static String codeToName(Integer code){
            RetStatus o = fromCode(code);
            return o.name();
        }

    public static Integer nameToCode(String name){
        RetStatus o = fromName(name);
        return o.getCode();
    }
}
