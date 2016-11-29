package com.ccnode.codegenerator.pojo;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/10/16 21:35
 */
public class PojoLine {

    String rawLine;
    String preWhiteSpace;
    String className;
    String variableName;
    Integer lineNumber;
    Integer lineStartPos;

    public String getRawLine() {
        return rawLine;
    }

    public void setRawLine(String rawLine) {
        this.rawLine = rawLine;
    }

    public Integer getLineStartPos() {
        return lineStartPos;
    }

    public void setLineStartPos(Integer lineStartPos) {
        this.lineStartPos = lineStartPos;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getPreWhiteSpace() {
        return preWhiteSpace;
    }

    public void setPreWhiteSpace(String preWhiteSpace) {
        this.preWhiteSpace = preWhiteSpace;
    }

}
