package com.ccnode.codegenerator.pojo;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/29 15:48
 */
public class ListInfo<T> {

    private List<T> fullList;
    private List<T> selectSegments;
    int startPos; //include
    int endPos; //exclude
    private List<T> newSegments;
    private List<T> newList;

    public List<T> getFullList() {
        return fullList;
    }

    public void setFullList(List<T> fullList) {
        this.fullList = fullList;
    }

    public List<T> getSelectSegments() {
        return selectSegments;
    }

    public void setSelectSegments(List<T> selectSegments) {
        this.selectSegments = selectSegments;
    }

    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public int getEndPos() {
        return endPos;
    }

    public void setEndPos(int endPos) {
        this.endPos = endPos;
    }

    public List<T> getNewSegments() {
        return newSegments;
    }

    public void setNewSegments(List<T> newSegments) {
        this.newSegments = newSegments;
    }

    public List<T> getNewList() {
        return newList;
    }

    public void setNewList(List<T> newList) {
        this.newList = newList;
    }

    public void setPos(Pair<Integer,Integer> pos) {
        this.startPos = pos.getLeft();
        this.endPos = pos.getRight();
        this.selectSegments = fullList.subList(startPos,endPos);
    }
}
