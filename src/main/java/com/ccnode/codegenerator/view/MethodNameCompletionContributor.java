package com.ccnode.codegenerator.view;

import com.ccnode.codegenerator.util.LoggerWrapper;
import com.ccnode.codegenerator.util.PsiElementUtil;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Lists;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionInitializationContext;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import com.intellij.psi.util.PsiClassUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

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
    public void beforeCompletion(@NotNull CompletionInitializationContext context) {
        //todo maybe need to add the method to check.
    }

    private static ImmutableListMultimap<String, String> multimap = ImmutableListMultimap.<String, String>builder()
            .put("s", "select")
            .put("S", "SELECT")
            .put("i", "insert into")
            .put("I", "INSERT INTO")
            .put("u", "update")
            .put("U", "UPDATE")
            .put("d", "delete")
            .put("D", "DELETE")
            .put("j", "join")
            .put("J", "JOIN")
            .put("i", "inner join")
            .put("I", "INNER JOIN")
            .put("l", "left join")
            .put("L", "LEFT JOIN")
            .put("o", "on")
            .put("O", "ON")
            .put("m", "max")
            .put("M", "MAX")
            .put("m", "min")
            .put("M", "MIN")
            .put("c", "count")
            .put("C", "COUNT")
            .put("d", "distinct")
            .put("D", "DISTINCT")
            .put("f", "from")
            .put("F", "FROM")
            .put("o", "order by")
            .put("O", "ORDER BY")
            .put("d", "desc")
            .put("d", "DESC")
            .put("w", "where")
            .put("W", "WHERE")
            .put("r", "right join")
            .put("R", "RIGHT JOIN")
            .put("l", "limit")
            .put("L", "LIMIT")
            .put("h", "having")
            .put("H", "HAVING")
            .put("g", "group by")
            .put("G", "GROUP BY")
            .put("v", "values")
            .put("V", "VALUES")
            .put("d", "duplicate")
            .put("D", "DUPLICATE")
            .put("f", "for update")
            .put("F", "FOR UPDATE")
            .put("a", "asc")
            .put("A", "ASC")
            .put("u", "union")
            .put("U", "UNION")
            .put("r", "replace")
            .put("R", "REPLACE")
            .put("u", "using")
            .put("U", "USING")
            .build();

    private static List<String> jdbcType = Lists.newArrayList("CHAR", "VARCHAR", "LONGVARCHAR", "BIT", "TINYINT", "SMALLINT", "INTEGER", "BIGINT", "REAL"
            , "DOUBLE", "FLOAT", "DECIMAL", "NUMERIC", "DATE", "TIME", "TIMESTAMP");


   @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        if (parameters.getCompletionType() != CompletionType.BASIC) {
            return;
        }
        PsiElement element = parameters.getPosition();
        PsiElement originalPosition = parameters.getOriginalPosition();
        PsiFile topLevelFile = InjectedLanguageUtil.getTopLevelFile(element);
        if (topLevelFile == null || !(topLevelFile instanceof PsiJavaFile)) {
            return;
        }
        PsiClass containingClass = PsiElementUtil.getContainingClass(originalPosition);
        if (containingClass == null || !containingClass.isInterface()) {
            return;
        }
        String text = originalPosition.getText();
        if (checkValidTextStarter(text)) {
            List<String> formatProps = new ArrayList<String>();
            List<String> strings = Lists.newArrayList("userName","TestString","gogo");
            for (String s : strings) {
                result.addElement(LookupElementBuilder.create(s));
            }
        }
    }

    public static boolean checkValidTextStarter(String text) {
        return text.startsWith("find")
                || text.startsWith("select")
                || text.startsWith("update")
                || text.startsWith("count");
    }

}
