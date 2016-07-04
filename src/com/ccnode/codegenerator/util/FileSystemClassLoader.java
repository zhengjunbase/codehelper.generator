package com.ccnode.codegenerator.util;

import org.jetbrains.annotations.Nullable;

import java.io.*;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/25 21:45
 */
public class FileSystemClassLoader extends ClassLoader {

    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classData = getClassData(name);
        if (classData == null) {
            throw new ClassNotFoundException();
        } else {
//            name = "TestPojo.class";
            String className = "com/qunar/insurance/statistic/dao/model/BlackListRecord.class";
            className = className.replace("/",".");
            return defineClass(className, classData, 0, classData.length);
        }
    }

    @Nullable
    private byte[] getClassData(String path) {
        try {
            InputStream ins = new FileInputStream(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int bufferSize = 4096;
            byte[] buffer = new byte[bufferSize];
            int bytesNumRead = 0;
            while ((bytesNumRead = ins.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesNumRead);
            }

            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String classNameToPath(String className) {
        return "/Users/zhengjun/Workspaces/genCodeSpace/MybatisGenerator/src/com/ccnode/codegenerator/test/TestPojoDao.java";
    }

    public static void main(String[] args) throws ClassNotFoundException {
        String path = "/Users/zhengjun/Workspaces/inspace/insurance_risk/src/main/java/com/qunar/insurance/statistic/dao/model/BlackListRecord.class";
        FileSystemClassLoader loader = new FileSystemClassLoader();
        loader.findClass(path);

    }
}