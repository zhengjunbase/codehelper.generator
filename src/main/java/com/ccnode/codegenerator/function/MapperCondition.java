package com.ccnode.codegenerator.function;

import com.ccnode.codegenerator.util.GenCodeUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/29 16:37
 */
public class MapperCondition implements EqualCondition<String> {

    @Override
    public boolean isEqual(String line , String word) {
        return StringUtils.contains(
                    StringUtils.deleteWhitespace(line),
                StringUtils.deleteWhitespace(word));
    }
}
