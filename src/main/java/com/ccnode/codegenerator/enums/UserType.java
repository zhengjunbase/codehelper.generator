package com.ccnode.codegenerator.enums;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/21 22:32
 */
public enum UserType {
    FREE_USER(0,""),
    PAID_YEAR_USER(1,""),
    NONE(-1,"none");

    private Integer code;
    private String desc;

    public boolean isFreeUser(){
        return this == FREE_USER;
    }

    private UserType(Integer code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static UserType fromName(String name){
        for(UserType e : UserType.values()){
            if (e.name().equals(name)){
                return e;
            }
        }
        return UserType.NONE;
    }

    public static UserType fromCode(Integer code){
        for(UserType e : UserType.values()){
            if (e.getCode().equals(code)){
                return e;
            }
        }
        return UserType.NONE;
    }

    public static String codeToName(Integer code){
            UserType o = fromCode(code);
            return o.name();
        }

    public static Integer nameToCode(String name){
        UserType o = fromName(name);
        return o.getCode();
    }
}
