package com.ccnode.codegenerator.pojoHelper;

import com.ccnode.codegenerator.enums.FileType;
import com.ccnode.codegenerator.pojo.GenCodeResponse;
import com.ccnode.codegenerator.pojo.GeneratedFile;
import com.ccnode.codegenerator.pojo.OnePojoInfo;
import com.intellij.vcs.log.ui.filter.FlatSpeedSearchPopup;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.svg.SVGFilterPrimitiveStandardAttributes;

import java.awt.*;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/22 15:15
 */
public class GenCodeResponseHelper {


    public static GeneratedFile getByFileType(@NotNull OnePojoInfo onePojoInfo, FileType type){
        for (GeneratedFile generatedFile : onePojoInfo.getFiles()) {
            if(generatedFile.getFileType() == type){
                return generatedFile;
            }
        }
        // todo
        throw new RuntimeException("获取文件错误");
    }
}
