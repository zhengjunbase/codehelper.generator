package com.ccnode.codegenerator.pojo;

import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/17 19:41
 */
public class PojoFieldInfo {

    String fieldName;
    String fieldClass;
    String fieldComment;
    List<Annotation> annotations;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(@Nullable String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldClass() {
        return fieldClass;
    }

    public void setFieldClass(String fieldClass) {
        this.fieldClass = fieldClass;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    public String getFieldComment() {
        return fieldComment;
    }

    public void setFieldComment(String fieldComment) {
        this.fieldComment = fieldComment;
    }
}
