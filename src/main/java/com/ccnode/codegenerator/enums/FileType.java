package com.ccnode.codegenerator.enums;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/21 22:54
 */
public enum FileType {
    SQL(0,".sql"),
    MAPPER(1,"Dao.xml"),
    SERVICE(2,"Service.java"),
    DAO(3,"Dao.java"),
    NONE(-1,"none");

    private Integer code;
    private String suffix;

    private FileType(Integer code,String suffix){
        this.code = code;
        this.suffix = suffix;
    }

    public Integer getCode() {
        return code;
    }

    public String getSuffix() {
        return suffix;
    }

    public static FileType fromName(String name){
        for(FileType e : FileType.values()){
            if (e.name().equals(name)){
                return e;
            }
        }
        return FileType.NONE;
    }

    public static FileType fromCode(Integer code){
        for(FileType e : FileType.values()){
            if (e.getCode().equals(code)){
                return e;
            }
        }
        return FileType.NONE;
    }

    public static String codeToName(Integer code){
            FileType o = fromCode(code);
            return o.name();
        }

    public static Integer nameToCode(String name){
        FileType o = fromName(name);
        return o.getCode();
    }
}
