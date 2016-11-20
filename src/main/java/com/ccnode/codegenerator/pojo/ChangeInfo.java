package com.ccnode.codegenerator.pojo;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/07/29 22:09
 */
public class ChangeInfo {
    String fileName;
    Integer affectRow;
    String changeType;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getAffectRow() {
        return affectRow;
    }

    public void setAffectRow(Integer affectRow) {
        this.affectRow = affectRow;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }
}
