package com.ccnode.codegenerator.pojo;

import com.ccnode.codegenerator.enums.FileType;

import java.io.File;
import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/17 19:55
 */
public class GeneratedFile {

    File file;
    String filePath;
    List<String> newLines;
    List<String> oldLines;
    List<String> originLines;
    FileType fileType;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<String> getNewLines() {
        return newLines;
    }

    public void setNewLines(List<String> newLines) {
        this.newLines = newLines;
    }

    public List<String> getOldLines() {
        return oldLines;
    }

    public void setOldLines(List<String> oldLines) {
        this.oldLines = oldLines;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public List<String> getOriginLines() {
        return originLines;
    }

    public void setOriginLines(List<String> originLines) {
        this.originLines = originLines;
    }
}
