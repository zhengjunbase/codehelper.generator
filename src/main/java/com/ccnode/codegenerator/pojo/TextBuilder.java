package com.ccnode.codegenerator.pojo;

import com.ccnode.codegenerator.util.GenCodeUtil;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2017/12/16 19:51
 */
public class TextBuilder {

    List<StringBuilder> lines = Lists.newArrayList();

    public void append(String text){
        if(lines.isEmpty()){
            lines.add(new StringBuilder(text));
        }else{
            lines.get(lines.size() - 1).append(text);
        }
    }
    public void appendLine(String line){
        lines.add(new StringBuilder(line));
    }
    public void appendLine(int index, String line ){
        lines.add(index, new StringBuilder(line));
    }

    public void startNewLine(){
        lines.add(new StringBuilder());
    }



    public void addRetract(String retract){
        for (StringBuilder line : lines) {
            line.insert(0, retract);
        }
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (StringBuilder line : lines) {
            builder.append(line);
            builder.append("\n");
        }
        return builder.toString();
    }
}
