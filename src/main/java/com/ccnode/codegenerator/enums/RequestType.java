package com.ccnode.codegenerator.enums;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/07/05 16:24
 */
public enum RequestType {

    REGISTER(0,"注册"),
    GEN_CODE(1,""),
    NONE(-1,"none");

    private Integer code;
    private String desc;

    private RequestType(Integer code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static RequestType fromName(String name){
        for(RequestType e : RequestType.values()){
            if (e.name().equals(name)){
                return e;
            }
        }
        return RequestType.NONE;
    }

    public static RequestType fromCode(Integer code){
        for(RequestType e : RequestType.values()){
            if (e.getCode().equals(code)){
                return e;
            }
        }
        return RequestType.NONE;
    }

    public static String codeToName(Integer code){
            RequestType o = fromCode(code);
            return o.name();
        }

    public static Integer nameToCode(String name){
        RequestType o = fromName(name);
        return o.getCode();
    }

    public Boolean equalWithName(String name){
        return this == fromName(name);
    }

    public Boolean equalWithCode(Integer code){
        return this == fromCode(code);
    }
}
