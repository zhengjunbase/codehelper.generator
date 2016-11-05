package com.ccnode.codegenerator.pojoHelper;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/11/04 20:17
 */
public class ProjectHelper {

    public static String getProjectPath(AnActionEvent event){
        Project project = event.getProject();
        @Nullable String projectPath = project.getBasePath();
        if(projectPath == null){
            projectPath = StringUtils.EMPTY;
        }
        if(StringUtils.isBlank(projectPath)){
            throw new RuntimeException("error, projectPath is empty");
        }
        String pathSplitter = System.getProperty("file.separator");
                if(!projectPath.endsWith(pathSplitter)){
                    projectPath += pathSplitter;
                }
        return projectPath;
    }
}
