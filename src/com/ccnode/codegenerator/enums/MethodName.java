package com.ccnode.codegenerator.enums;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/08/27 17:12
 */
public enum MethodName {

    insert(0,"插入"),
    insertList(1,"插入"),
    select(2,"插入"),
    update(3,"插入"),
    delete(4,"插入"),
    none(-1,"none");

    private Integer code;
    private String desc;

    private MethodName(Integer code,String desc){
    this.code = code;
    this.desc = desc;
    }

    public Integer getCode() {
    return code;
    }

    public String getDesc() {
    return desc;
    }

    public static MethodName fromName(String name){
    for(MethodName e : MethodName.values()){
        if (e.name().equals(name)){
            return e;
        }
    }
    return MethodName.none;
    }

    public static MethodName fromCode(Integer code){
    for(MethodName e : MethodName.values()){
        if (e.getCode().equals(code)){
            return e;
        }
    }
    return MethodName.none;
    }

    public static String codeToName(Integer code){
        MethodName o = fromCode(code);
        return o.name();
    }

    public static Integer nameToCode(String name){
    MethodName o = fromName(name);
    return o.getCode();
    }

    public Boolean equalWithName(String name){
        return this == fromName(name);
    }

    public Boolean equalWithCode(String name){
        return this == fromName(name);
    }
}
