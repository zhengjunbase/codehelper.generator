package com.ccnode.codegenerator.enums;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/21 22:51
 */
public enum GenCodeStrategy {

    APPEND_NEW(0,""),
    REPLACE_TOTALLY(1,""),
    NONE(-1,"none");

    private Integer code;
    private String desc;

    private GenCodeStrategy(Integer code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static GenCodeStrategy fromName(String name){
        for(GenCodeStrategy e : GenCodeStrategy.values()){
            if (e.name().equals(name)){
                return e;
            }
        }
        return GenCodeStrategy.NONE;
    }

    public static GenCodeStrategy fromCode(Integer code){
        for(GenCodeStrategy e : GenCodeStrategy.values()){
            if (e.getCode().equals(code)){
                return e;
            }
        }
        return GenCodeStrategy.NONE;
    }

    public static String codeToName(Integer code){
            GenCodeStrategy o = fromCode(code);
            return o.name();
        }

    public static Integer nameToCode(String name){
        GenCodeStrategy o = fromName(name);
        return o.getCode();
    }
}
