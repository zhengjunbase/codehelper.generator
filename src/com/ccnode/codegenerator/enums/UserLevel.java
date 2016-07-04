package com.ccnode.codegenerator.enums;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/21 22:32
 */
public enum UserLevel {
    FREE_USER(0,""),
    YEAR_USER(1,""),
    NONE(-1,"none");

    private Integer code;
    private String desc;

    public boolean isFreeUser(){
        return this == FREE_USER;
    }

    private UserLevel(Integer code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static UserLevel fromName(String name){
        for(UserLevel e : UserLevel.values()){
            if (e.name().equals(name)){
                return e;
            }
        }
        return UserLevel.NONE;
    }

    public static UserLevel fromCode(Integer code){
        for(UserLevel e : UserLevel.values()){
            if (e.getCode().equals(code)){
                return e;
            }
        }
        return UserLevel.NONE;
    }

    public static String codeToName(Integer code){
            UserLevel o = fromCode(code);
            return o.name();
        }

    public static Integer nameToCode(String name){
        UserLevel o = fromName(name);
        return o.getCode();
    }
}
