package com.ccnode.codegenerator.view;

import com.ccnode.codegenerator.genCode.genFind.SqlWordType;
import com.ccnode.codegenerator.pojo.OnePojoInfo;
import com.ccnode.codegenerator.pojo.PojoFieldInfo;
import com.ccnode.codegenerator.pojoHelper.OnePojoInfoHelper;
import com.ccnode.codegenerator.util.GenCodeUtil;
import com.ccnode.codegenerator.util.IOUtils;
import com.ccnode.codegenerator.util.LogHelper;
import com.ccnode.codegenerator.util.LoggerWrapper;
import com.ccnode.codegenerator.util.PsiElementUtil;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionService;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * What always stop you is what you always believe.
 *
 * Created by zhengjun.du on 2017/08/11 21:30
 */
public class MethodNameCompletionContributor extends CompletionContributor {

    private final static Logger LOGGER = LoggerWrapper.getLogger(MethodNameCompletionContributor.class);

   @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
       Project project = parameters.getEditor().getProject();
      if (parameters.getCompletionType() != CompletionType.BASIC) {
            return;
        }
        PsiElement element = parameters.getPosition();
        PsiElement originalPosition = parameters.getOriginalPosition();
        PsiFile topLevelFile = InjectedLanguageUtil.getTopLevelFile(element);
        if (topLevelFile == null || originalPosition == null || !(topLevelFile instanceof PsiJavaFile)) {
            return;
        }
        PsiClass containingClass = PsiElementUtil.getContainingClass(originalPosition);
        if (containingClass == null || !containingClass.isInterface()) {
            return;
        }
        String text = originalPosition.getText();
        OnePojoInfo onePojoInfo = OnePojoInfoHelper.parseOnePojoInfoFromClass(containingClass, project);

       if (checkValidTextStarter(text)) {
           List<String> recommend = buildRecommend(text, onePojoInfo.getPojoFieldInfos());
           String keepText = text;
           if(recommend.isEmpty()){
               keepText = text.substring(0,text.length() -1);
               List<String> results = buildRecommend(keepText, onePojoInfo.getPojoFieldInfos());
               for (String s : results) {
                   if(StringUtils.startsWithIgnoreCase(s, text.substring(text.length() - 1))){
                       recommend.add(s);
                   }
               }
           }
           if(recommend.isEmpty()){
               keepText = text.substring(0,text.length() - 2);
               List<String> results = buildRecommend(keepText, onePojoInfo.getPojoFieldInfos());
               for (String s : results) {
                   if(StringUtils.startsWithIgnoreCase(s, text.substring(text.length() - 2))){
                       recommend.add(s);
                   }
               }
           }
           for (String each : recommend) {
                result.addElement(LookupElementBuilder.create(keepText + each));
            }
        }
       LoggerWrapper.saveAllLogs(project.getBasePath());
   }

    private List<String> buildRecommend(String text, List<PojoFieldInfo> pojoFieldInfos) {
        List<String> keyWords = Lists.newArrayList();
        List<String> fields = Lists.newArrayList();
        List<String> retList = Lists.newArrayList();
        LOGGER.info(" buildRecommend  :{}" + text + LogHelper.toString(fields));
        for (PojoFieldInfo pojoFieldInfo : pojoFieldInfos) {
            fields.add(GenCodeUtil.getUpperCamel(pojoFieldInfo.getFieldName()));
        }
        for (SqlWordType sqlWordType : SqlWordType.values()) {
            if(SqlWordType.START_WORD_SET.contains(sqlWordType)
                    || sqlWordType == SqlWordType.None){
                continue;
            }
            keyWords.add(sqlWordType.name().toUpperCase());
        }
        boolean endWithField = false;
        for (String each : fields) {
            if(text.endsWith(each)){
                endWithField = true;
            }
        }
        LOGGER.info(" buildRecommend  :{}" + text + endWithField);
        SqlWordType current = null;
        if(endWithField){
            current = SqlWordType.Field;
        }else{
             for (SqlWordType sqlWordType : SqlWordType.values()) {
                if( sqlWordType != SqlWordType.None){
                    if(StringUtils.endsWithIgnoreCase(text, sqlWordType.name())){
                        current = sqlWordType;
                    }
                }
             }
        }
        if(current == null){
            return Lists.newArrayList();
        }

        List<SqlWordType> canFollowList = current.getCanFollowList();
        for (SqlWordType sqlWordType : canFollowList) {
            if(sqlWordType == SqlWordType.Field){
                retList.addAll(fields);
            }else{
                retList.add(GenCodeUtil.getUpperCamel(sqlWordType.name()));
            }
        }
        LOGGER.info(" buildRecommend  :{}" + text + LogHelper.toString(retList));
        return retList;
    }

    public static boolean checkValidTextStarter(String text) {
        return text.startsWith("find")
                || text.startsWith("select")
                || text.startsWith("update")
                || text.startsWith("count");
    }

    public static void main(String[] args) {
        System.out.println(Splitter.onPattern("<|>").trimResults().omitEmptyStrings()
                        .splitToList("List<String> fuck"));
        System.out.println(Splitter.onPattern("<|>|\\s").trimResults().omitEmptyStrings()
                        .splitToList("Type use fsd"));
    }
}
