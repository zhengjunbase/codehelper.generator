package com.ccnode.codegenerator.genCode.genFind;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2017/08/04 19:34
 */
public enum SqlFragmentType {
    KEY_WORD_WITH_FIELD(0,""),
    KEY_WORD_WITH_NUMBER(0,""),
    SINGLE_KEY_WORD(1,""),
    NONE(-1,"none");

    private Integer code;
    private String desc;



    private SqlFragmentType(Integer code,String desc){
    this.code = code;
    this.desc = desc;
    }

    public Integer getCode() {
    return code;
    }

    public String getDesc() {
    return desc;
    }

    public static SqlFragmentType fromName(String name){
        for(SqlFragmentType e : SqlFragmentType.values()){
            if (e.name().equals(name)){
                return e;
            }
        }
        return SqlFragmentType.NONE;
    }

    public static SqlFragmentType fromCode(Integer code){
        for(SqlFragmentType e : SqlFragmentType.values()){
            if (e.getCode().equals(code)){
                return e;
            }
        }
        return SqlFragmentType.NONE;
    }

    public static SqlFragmentType fromDesc(String desc){
        for(SqlFragmentType e : SqlFragmentType.values()){
            if (e.getDesc().equals(desc)){
                return e;
            }
        }
        return SqlFragmentType.NONE;
    }

    public static String codeToName(Integer code){
        SqlFragmentType o = fromCode(code);
        return o.name();
    }


}
