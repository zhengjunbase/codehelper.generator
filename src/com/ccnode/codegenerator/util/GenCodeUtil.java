package      com . ccnode.codegenerator.util ;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/21 22:05
 */
public class GenCodeUtil {
    
    public static final String ONE_RETRACT = "    ";
    public static final String TWO_RETRACT = "        ";
    public static final String THREE_RETRACT = "            ";
    public static final String FOUR_RETRACT = "                ";
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

    @Nullable
    public static Class loadClassByFileName(@NotNull String dir, @NotNull String pojoName){
        String fullPojoPath = IOUtils.matchOnlyOneFile(dir, pojoName +  ".java").getAbsolutePath();
        if(StringUtils.isBlank(fullPojoPath)){
            return null;
        }
        try{
            fullPojoPath = "/Users/zhengjun/Workspaces/inspace/insurance_risk/src/main/java/com/qunar/insurance/statistic/dao/model/BlackListRecord.java";
            dir = "/Users/zhengjun/Workspaces/inspace/insurance_risk";
            Runtime.getRuntime().exec("javac " + fullPojoPath);
//            dir = "/Users/zhengjun/Workspaces/inspace/insurance_risk/src/main/java/com/qunar/insurance/statistic/dao/model/BlackListRecord.class";
//            dir = "/Users/zhengjun/tmp";
            File file = new File(dir);
            java.net.URL url = file.toURL();
            java.net.URL[] urls = new java.net.URL[]{url};
            URLClassLoader loader = new URLClassLoader(urls);
            String path = getPojoPackage(fullPojoPath)+ "." + pojoName;
            path = "/Users/zhengjun/Workspaces/inspace/insurance_risk/src/main/java/com/qunar/insurance/statistic/dao/model/BlackListRecord";
            Class<?> aClass = loader.loadClass(path);

            return aClass;
        }catch(Exception e){
            System.out.println("load class By file Name error");
            return null;
        }finally {
            String clazzPath = fullPojoPath.replace(".java", ".class");
            File file = new File(clazzPath);
            file.deleteOnExit();
        }
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
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL,value);
    }

    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException {

          ClassLoader cl = ClassLoader.getSystemClassLoader();

        URL[] urls = ((URLClassLoader)cl).getURLs();

        for(URL url: urls){
        	System.out.println(url.getFile());
        }

        Class testPojo = loadClassByFileName(
                "/Users/zhengjun/Workspaces/genCodeSpace/MybatisGenerator/src/com/ccnode/codegenerator/test/",
                "TestPojo");

        System.out.println(testPojo);
    }
}
