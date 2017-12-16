package com.ccnode.codegenerator.enums;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2017/12/09 18:41
 */
public enum SupportFieldClass {
    STRING(0,"java.lang.String","String"),
    DATE(1,"java.util.Date","Date"),
    BIG_DECIMAL(2,"java.math.BigDecimal","BigDecimal"),
    INTEGER(3,"java.lang.Integer", "Integer"),
    LONG(4,"java.lang.Long", "Long"),
    SHORT(5,"java.lang.Short", "Short"),
    DOUBLE(6,"java.lang.Double", "Double"),
    FLOAT(7,"java.lang.Float", "Float"),
    JAVA_SQL_TIMESTAMP(8,"java.sql.Timestamp", "Timestamp"),
    JAVA_SQL_Date(9,"java.sql.Date", "Date"),
    BASIC_INT(10,"int", "int"),
    BASIC_LONG(11,"long", "long"),
    BASIC_SHORT(12,"short", "short"),
    BASIC_DOUBLE(13,"double", "double"),
    BASIC_FLOAT(14,"float", "float"),
    NONE(-1,"none", "none");

    private Integer code;
    private String canonicalText;
    private String presentableText;

    private SupportFieldClass(Integer code, String canonicalText, String presentableText){
        this.code = code;
        this.canonicalText = canonicalText;
        this.presentableText = presentableText;
    }

    public String getPresentableText() {
        return presentableText;
    }

    public Integer getCode() {
        return code;
    }

    public String getCanonicalText() {
        return canonicalText;
    }

    public static SupportFieldClass fromName(String name){
        for(SupportFieldClass e : SupportFieldClass.values()){
            if (e.name().equals(name)){
                return e;
            }
        }
        return SupportFieldClass.NONE;
    }

    public static SupportFieldClass fromCode(Integer code){
        for(SupportFieldClass e : SupportFieldClass.values()){
            if (e.getCode().equals(code)){
                return e;
            }
        }
        return SupportFieldClass.NONE;
    }

    public static SupportFieldClass fromDesc(String desc){
        for(SupportFieldClass e : SupportFieldClass.values()){
            if (e.getCanonicalText().equalsIgnoreCase(desc)){
                return e;
            }
        }
        return SupportFieldClass.NONE;
    }


}
