/* FileUtil.java
 * 
 * Copyright (c) 2014 Qunar.com. All Rights Reserved. */
package com.ccnode.codegenerator.util;

import com.google.common.base.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author bin.qiao
 */
public class FileUtil {

    private final static Logger LOGGER = LoggerWrapper.getLogger(FileUtil.class);

    public static Iterable<String> readFile(String filename) throws IOException {
        return readFile(new File(filename));

    }
    public static Iterable<String> readFile(File file) throws IOException {
        final LineIterator lineIterator = FileUtils.lineIterator(file, Charsets.UTF_8.name());
        return new Iterable<String>() {
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    public boolean hasNext() {
                        boolean hasNext = lineIterator.hasNext();
                        if (!hasNext) {
                            LineIterator.closeQuietly(lineIterator);
                        }
                        return hasNext;
                    }

                    public String next() {
                        if (!hasNext()) {
                            throw new NoSuchElementException("No more lines");
                        }
                        return (String)lineIterator.next();
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    public static File zip(File file) throws Exception {
        BufferedInputStream bis = null;
        String zipFilePath = file.getPath() + ".zip";
        FileOutputStream fos = null;
        ZipOutputStream out = null;
        byte data[] = new byte[2048];
        try {
            fos = new FileOutputStream(zipFilePath);
            out = new ZipOutputStream(new BufferedOutputStream(fos));
            FileInputStream fi = new FileInputStream(file);
            bis = new BufferedInputStream(fi, 2048);
            ZipEntry entry = new ZipEntry(file.getName());
            out.putNextEntry(entry);
            int count;
            while ((count = bis.read(data, 0, 2048)) != -1) {
                out.write(data, 0, count);
            }
            bis.close();

        }catch(Exception e){
            throw e;
        }finally {
            org.apache.commons.io.IOUtils.closeQuietly(out);
            org.apache.commons.io.IOUtils.closeQuietly(fos);
            if(file != null && file.exists())
                file.delete();
        }
        return new File(zipFilePath);
    }
}
