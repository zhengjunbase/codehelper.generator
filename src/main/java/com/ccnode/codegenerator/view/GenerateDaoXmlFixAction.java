package com.ccnode.codegenerator.view;

import com.intellij.codeInsight.daemon.QuickFixBundle;
import com.intellij.codeInsight.daemon.impl.DaemonCodeAnalyzerEx;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.daemon.impl.quickfix.CreateFromUsageBaseFix;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.util.Processor;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by bruce.ge on 2016/12/6.
 */
@Deprecated
public class GenerateDaoXmlFixAction extends CreateFromUsageBaseFix {

    public static final String GENERATE_METHOD_SQL = "generate method sql";
    private final SmartPsiElementPointer myMethodCall;

    public GenerateDaoXmlFixAction(PsiMethodCallExpression methodCall) {
        myMethodCall = SmartPointerManager.getInstance(methodCall.getProject()).createSmartPsiElementPointer(methodCall);
    }

    @Override
    protected boolean isAvailableImpl(int offset) {
        final PsiMethodCallExpression call = getMethodCall();
        if (call == null || !call.isValid()) return false;
        PsiReferenceExpression ref = call.getMethodExpression();
        String name = ref.getReferenceName();
        if (name == null || !PsiNameHelper.getInstance(ref.getProject()).isIdentifier(name)) return false;
        if (hasErrorInArgumentList(call)) return false;
        return true;
    }

    private boolean hasErrorInArgumentList(PsiMethodCallExpression call) {
        Project project = call.getProject();
        Document document = PsiDocumentManager.getInstance(project).getDocument(call.getContainingFile());
        if (document == null) return true;
        PsiExpressionList argumentList = call.getArgumentList();
        final TextRange argRange = argumentList.getTextRange();
        return !DaemonCodeAnalyzerEx.processHighlights(document, project, HighlightSeverity.ERROR,
                argRange.getStartOffset() + 1, argRange.getEndOffset() - 1, new Processor<HighlightInfo>() {
                    @Override
                    public boolean process(HighlightInfo highlightInfo) {
                        return !(highlightInfo.getActualStartOffset() > argRange.getStartOffset() && highlightInfo.getActualEndOffset()
                                < argRange.getEndOffset());
                    }
                });
    }


    @Override
    protected void invokeImpl(PsiClass targetClass) {
        if(targetClass==null) return;
        PsiMethodCallExpression expression = getMethodCall();
        if(expression==null) return;
        PsiReferenceExpression ref = expression.getMethodExpression();
        String methodName = ref.getReferenceName();

    }

    @Override
    protected boolean isValidElement(PsiElement element) {
        PsiMethodCallExpression callExpression = (PsiMethodCallExpression) element;
        PsiReferenceExpression referenceExpression = callExpression.getMethodExpression();
        return true;
    }

    @Nullable
    @Override
    protected PsiElement getElement() {
        return null;
    }

    @NotNull
    @Override
    public String getText() {
        return GENERATE_METHOD_SQL;
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return QuickFixBundle.message("create.method.from.usage.family");
    }

    public PsiMethodCallExpression getMethodCall() {
        return (PsiMethodCallExpression) myMethodCall.getElement();
    }
}
