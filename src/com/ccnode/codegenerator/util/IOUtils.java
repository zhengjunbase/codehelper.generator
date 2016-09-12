package com.ccnode.codegenerator.util;

import com.ccnode.codegenerator.exception.BizException;
import com.ccnode.codegenerator.pojoHelper.GenCodeResponseHelper;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.io.CharSink;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhengjun.du on 7/15/15.
 * mail zhengjun.du@qunar.com
 */
public class IOUtils {

    public static final String[] DEFAULT_FILE_TYPE = null;
    public static final Charset DEFAULT_CHARSET = Charsets.UTF_8;
//    public static void writeLine

    public static String parseFromPattern(String s,String patternString){
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            return matcher.group();
        }else {
            return StringUtils.EMPTY;
        }
    }
    public static <T> List<T> readLines(File file, LineProcessor<T> lineProcessor) throws IOException {
        List<T> results = Lists.newArrayList();
            ImmutableList<String> lineList = Files.asCharSource(file, Charsets.UTF_8).readLines();
            for (String line :lineList){
                T t = lineProcessor.processLine(line);
                if(t != null) {
                    results.add(t);
                }
            }
        return results;
    }
    public static <T> List<T> readLines(List<String> lines, LineProcessor<T> lineProcessor) throws IOException {
        List<T> results = Lists.newArrayList();
            for (String line :lines){
                T t = lineProcessor.processLine(line);
                if(t != null) {
                    results.add(t);
                }
            }
        return results;
    }




    public static List<String> readFile(String  fileName,LineMerger lineMerger) throws IOException {
        return readFile(fileName,DEFAULT_CHARSET,lineMerger);
    }


    public static List<String> readLines(File file) throws IOException {
        if(file == null){
            return Lists.newArrayList();
        }
        ImmutableList<String> lineList = Files.asCharSource(file, DEFAULT_CHARSET).readLines();
            return lineList;
    }
    public static List<String> readLines(String fileName) throws IOException {
        ImmutableList<String> lineList = Files.asCharSource(new File(fileName), DEFAULT_CHARSET).readLines();
            return lineList;
    }
    public static List<String> readFile(String  fileName, Charset charset,LineMerger lineMerger) throws IOException {
        ImmutableList<String> lineList = Files.asCharSource(new File(fileName), charset).readLines();
        return lineMerger.mergeLine(lineList);
    }

    public static <T> List<T> readLines(String fileName, LineProcessor<T> lineProcessor) throws IOException {
        File file = new File(fileName);
        Preconditions.checkNotNull(file);
        Preconditions.checkState(file.isFile());
        return readLines(file, lineProcessor);
    }

    public static  void writeLines(String fileName,List<String> list) throws IOException {
        writeLines(fileName, list, DEFAULT_CHARSET);
    }
    public static  void writeLines(String fileName,List<String> list,Charset charset) throws IOException {
        CharSink cs = Files.asCharSink(new File(fileName), charset);
        list = PojoUtil.avoidEmptyList(list);
        cs.writeLines(list);
    }

    public static  void writeLines(File file,List<String> list) throws IOException {
        writeLines(file, list, DEFAULT_CHARSET);
    }
    public static  void writeLines(File file,List<String> list,Charset charset) throws IOException {
        CharSink cs = Files.asCharSink(file, charset);
        list = PojoUtil.avoidEmptyList(list);
        cs.writeLines(list);
    }



    @Nullable
    public static File matchOnlyOneFile(String directory, String subFileName){
        List<File> allSubFiles = IOUtils.getAllSubFiles(directory);
        if(!subFileName.startsWith(GenCodeResponseHelper.getPathSplitter())){
            subFileName = GenCodeResponseHelper.getPathSplitter() + subFileName;
        }
        allSubFiles = PojoUtil.avoidEmptyList(allSubFiles);
        File configFile = null;
        String targetDirPrefix = GenCodeResponseHelper.getPathSplitter() + "target" + GenCodeResponseHelper.getPathSplitter();
        for (File subFile : allSubFiles) {
            if(!StringUtils.containsIgnoreCase(subFile.getAbsolutePath(), targetDirPrefix)
                    && StringUtils.endsWithIgnoreCase(subFile.getAbsolutePath(),subFileName)){
                if(configFile == null){
                    configFile = subFile;
                }else{
                    //todo 调试的时候加上.
//                    return "NOT_ONLY";
                    throw new BizException("not only one file:" + subFileName);
                }
            }
        }
        if(configFile == null){
            return null;
        }
        return configFile;
    }
    public static List<File> getAllSubFiles(File parentFile){
        return getAllSubFiles(parentFile,DEFAULT_FILE_TYPE);
    }

    public static List<File> getAllSubFiles(String fileName){
        Preconditions.checkNotNull(fileName);
        File  file = new File(fileName);
        Preconditions.checkNotNull(file);
        return getAllSubFiles(file);
    }
    public static List<File> getAllSubFiles(String fileName,String[] fileType) {
        Preconditions.checkNotNull(fileName);
        File  file = new File(fileName);
        Preconditions.checkNotNull(file);
        return getAllSubFiles(file,fileType);
    }
    public static List<File> getAllSubFiles(File parentFile,@Nullable String[] fileType) {
        List<File> fileList = Lists.newArrayList();
        if(parentFile.isDirectory()){
            fileList = Lists.newArrayList(FileUtils.listFiles(parentFile, fileType, true));
        }else if(parentFile.isFile()){
            fileList = Lists.newArrayList(parentFile);
        }
        return fileList;
    }

    public static <K, V extends Integer> void mapIncreaseKey(Map<K, V> map, K key){
        Integer value ;
        if((value = map.get(key)) == null){
            value = 0;
        }
        value ++;
        map.put(key, (V)(value));
    }

    public static <K,  V extends Comparable > List<Map.Entry<K, V>> sortMapByValue(Map<K, V> map){
        List<Map.Entry<K, V>> entryList = Lists.newArrayList(map.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        return entryList;
    }
    public static <K,  V  > List<Map.Entry<K, V>> sortMapByValue(Map<K, V> map, final Comparator<V> comparator){
        List<Map.Entry<K, V>> entryList = Lists.newArrayList(map.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return comparator.compare(o1.getValue(), o2.getValue());
            }
        });
        return entryList;
    }
    public static <K,  V extends Comparable > List<Map.Entry<K, V>> sortMapByValueDESC(Map<K, V> map){
        List<Map.Entry<K, V>> entryList = Lists.newArrayList(map.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        return entryList;
    }

    public static <K extends Comparable,  V > List<Map.Entry<K, V>> sortMapByKey(Map<K, V> map) {
        List<Map.Entry<K, V>> entryList = Lists.newArrayList(map.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        return entryList;
    }

    public static <K,V> List<Map.Entry<K, V>> sortMapByKey(Map<K, V> map, final Comparator<K> comparator){
        List<Map.Entry<K, V>> entryList = Lists.newArrayList(map.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<K,V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return comparator.compare(o1.getKey(),o2.getKey());
            }
        });
        return entryList;
    }
    public static <K extends Comparable, V > List<Map.Entry<K, V>> sortMapByKeyDESC(Map<K, V> map){
        List<Map.Entry<K, V>> entryList = Lists.newArrayList(map.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<K,V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return o2.getKey().compareTo(o1.getKey()) ;
            }
        });
        return entryList;
    }
}
