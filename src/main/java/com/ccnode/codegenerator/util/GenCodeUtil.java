package com.ccnode.codegenerator.util;

import com.google.common.base.CaseFormat;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/21 22:05
 */
public class GenCodeUtil {

    private final static Logger LOGGER = LoggerWrapper.getLogger(GenCodeUtil.class);

    public static final String ONE_RETRACT = "    ";
    public static final String TWO_RETRACT = "        ";
    public static final String THREE_RETRACT = "            ";
    public static final String FOUR_RETRACT = "                ";
    public static final String ONE_SPACE = " ";
    public static final String ONE_COMMA = ",";
    public static final String FIELD_SPLITTER = ", ";
    public static String MYSQL_TYPE = StringUtils.EMPTY;
    public static String PACKAGE_LINE = StringUtils.EMPTY;

    
    public static List<String> grapOld(@NotNull List<String> oldList, 
            @NotNull String startKeyWord, @NotNull String endKeyWord){
        int startIndex = -1;
        int endIndex= -1;
        int i = 0;
        for (String line : oldList) {
            if(sqlContain(line,startKeyWord)){
                startIndex = i;
            }
            if(sqlContain(line,endKeyWord)){
                endIndex = i;
            }
            i ++;
        }
        if(startIndex == -1 || endIndex == -1){
            return Lists.newArrayList();
        }
        return oldList.subList(startIndex, endIndex + 1);
    }

    public static String getPojoPackage(@NotNull String fullPojoFilePath){
        try{
            List<String> strings = IOUtils.readLines(fullPojoFilePath);
            for (String string : strings) {
                if(string.startsWith("package")){
                    string = string.replace(";", "");
                    string = string.replace(" ", "");
                    string = string.replace("package", "");
                    return string;
                }
            }
        }catch(Exception e){
            return "ERROR_PACKAGE";
        }
        return StringUtils.EMPTY;

    }

    public static boolean sqlContain(List<String> lines, @NotNull String word) {
        lines = PojoUtil.avoidEmptyList(lines);
        for (String line : lines) {
            if(sqlContain(line, word)){
                return true;
            }
        }
        return false;
    }

    public static String deducePackage(String daoFilePathString, String pojoPackage,String pojoPathString){
        //get the path by
        LOGGER.info("path:{}, pojoPackage:{}",daoFilePathString,pojoPackage);
        Path realpojoPath = Paths.get(pojoPathString);
        String[] split = pojoPackage.split("\\.");
        int len = split.length;
        Path sourcePath = realpojoPath;
        while(len>=0){
            sourcePath = sourcePath.getParent();
            len--;
        }
        Path daoFilePath = Paths.get(daoFilePathString);
        Path daoFolder = daoFilePath.getParent();
        //shall combine two path
        Path relativeToSouce = sourcePath.relativize(daoFolder);
        String relate = relativeToSouce.toString();
        relate = relate.replace("\\", ".");
        relate = relate.replace("/",".");
        return relate;
    }

    public static String pathToPackage(String path){
        path = path.replace("/",".");
        path = path.replace("\\",".");
        if(path.startsWith("src.main.java.")){
            path = path.replace("src.main.java.","");
        }
        if(path.startsWith("src.main.")){
            path = path.replace("src.main.","");
        }
        if(path.startsWith("src.")){
            path = path.replace("src.","");
        }
        if(path.startsWith(".")){
            path = path.substring(1,path.length());
        }
        return path;
    }



    public static boolean sqlContain(String sequence, @NotNull String word){
        if(StringUtils.isBlank(sequence)){
            return false;
        }
        return StringUtils.containsIgnoreCase(
                    StringUtils.deleteWhitespace(sequence),
                StringUtils.deleteWhitespace(word));
    }

    public static String getUnderScore(String value) {
        if(value == null){
            return StringUtils.EMPTY;
        }
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, value);
    }

    public static String getLowerCamel(String value){
        if(StringUtils.isBlank(value)){
            return StringUtils.EMPTY;
        }
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL,value);
    }

    public static String getUpperCamel(String value){
        if(StringUtils.isBlank(value)){
            return StringUtils.EMPTY;
        }
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL,value);
    }

    public static void recursiveRemoveEnd(StringBuilder builder, String compare){
        if(builder == null || compare == null
                || compare.length() < 1 || builder.length() < 1){
            return;
        }
        int remindLength = builder.length() - compare.length();
        while (remindLength >= 0 && StringUtils.equals(compare,
                builder.substring(remindLength, builder.length()))){
            builder.delete(remindLength, builder.length());
            remindLength = builder.length() - compare.length();
        }
    }

    public static void main(String[] args) {
        StringBuilder builder = new StringBuilder();
        builder.append("fsfasdfffdf");
        Integer length = builder.length();
        recursiveRemoveEnd(builder, "xdf");
        builder= new StringBuilder("b");
        recursiveRemoveEnd(builder, "");
        System.out.println(builder);
    }
}
