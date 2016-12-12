package com.ccnode.codegenerator.nextgenerationparser.parser;

import com.ccnode.codegenerator.jpaparse.KeyWordConstants;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class BaseParser {
    protected static String[] linkOp = {KeyWordConstants.AND, KeyWordConstants.OR};

    protected static String[] compareOp = {KeyWordConstants.BETWEEN, KeyWordConstants.GREATERTHAN, KeyWordConstants.LESSTHAN,
            KeyWordConstants.ISNOTNULL, KeyWordConstants.ISNULL, KeyWordConstants.NOTNULL, KeyWordConstants.NOTLIKE, KeyWordConstants.LIKE
            , KeyWordConstants.NOTIN, KeyWordConstants.NOT, KeyWordConstants.IN};

    protected static String[] order = {KeyWordConstants.ASC, KeyWordConstants.DESC};
}
