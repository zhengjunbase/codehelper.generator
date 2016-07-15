package com.ccnode.codegenerator.genCode;

import com.ccnode.codegenerator.enums.FileType;
import com.ccnode.codegenerator.pojo.GenCodeResponse;
import com.ccnode.codegenerator.pojo.GeneratedFile;
import com.ccnode.codegenerator.pojo.OnePojoInfo;
import com.ccnode.codegenerator.pojo.PojoFieldInfo;
import com.ccnode.codegenerator.pojoHelper.GenCodeResponseHelper;
import com.ccnode.codegenerator.util.DateUtil;
import com.ccnode.codegenerator.util.GenCodeUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.ccnode.codegenerator.util.GenCodeUtil.ONE_RETRACT;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/17 20:12
 */
public class GenSqlService {

    private final static Logger LOGGER = LoggerFactory.getLogger(GenSqlService.class);

    public static void genSQL(GenCodeResponse response) {
        try {
            for (OnePojoInfo onePojoInfo : response.getPojoInfos()) {
                genSQLFile(onePojoInfo, response);
            }

        } catch (Exception e) {
            LOGGER.error("GenSqlService genSQL error", e);
        }

    }

    private static void genSQLFile(OnePojoInfo onePojoInfo, GenCodeResponse response) {
        GeneratedFile fileInfo = GenCodeResponseHelper.getByFileType(onePojoInfo, FileType.SQL);
        Boolean canReplace = canReplace(fileInfo, response);
        if (fileInfo.getOldLines().isEmpty() || !canReplace) {
            List<String> newLines = genSql(onePojoInfo, response);
            fileInfo.setNewLines(newLines);
        }
        if (canReplace) {
            List<String> newLines = replaceSql(onePojoInfo, fileInfo, response);
            fileInfo.setNewLines(newLines);
        }
    }

    private static Boolean canReplace(@NotNull GeneratedFile fileInfo, GenCodeResponse response) {
        if (GenCodeUtil.sqlContain(fileInfo.getOldLines(), "CREATE TABLE") && GenCodeUtil
                .sqlContain(fileInfo.getOldLines(), ")ENGINE=")) {
            return true;
        }
        return false;
    }

    private static List<String> genSql(@NotNull OnePojoInfo onePojoInfo, GenCodeResponse response) {
        List<String> retList = Lists.newArrayList();
        String tableName = GenCodeUtil.getUnderScore(onePojoInfo.getPojoClassSimpleName());
        retList.add(String.format("-- auto Generated on %s ", DateUtil.formatLong(new Date())));
        retList.add("-- DROP TABLE IF EXISTS `" + tableName + "`; ");
        retList.add("CREATE TABLE " + tableName + "(");
        for (PojoFieldInfo field : onePojoInfo.getPojoFieldInfos()) {
            String fieldSql = genfieldSql(field, response);
            retList.add(fieldSql);
        }
        retList.add(ONE_RETRACT + "PRIMARY KEY (`id`)");
        retList.add(String.format(")ENGINE=%s DEFAULT CHARSET=%s COMMENT '%s';", getSqlEngine(response),
                getSqlCharSet(response), tableName));
        return retList;
    }

    private static String getSqlCharSet(GenCodeResponse response) {
        String charset = response.getUserConfigMap().get("charset");
        if (StringUtils.isBlank(charset)) {
            return "utf8";
        }
        return charset;
    }

    private static String getSqlEngine(GenCodeResponse response) {
        String sqlengine = response.getUserConfigMap().get("sqlengine");
        if (StringUtils.isBlank(sqlengine)) {
            return "InnoDB";
        }
        return sqlengine;
    }

    private static List<String> replaceSql(@NotNull OnePojoInfo onePojoInfo, GeneratedFile fileInfo, GenCodeResponse response) {
        List<String> oldList = fileInfo.getOldLines();
        int oldIndex = findFirstFieldPos(oldList);
        for (PojoFieldInfo fieldInfo : onePojoInfo.getPojoFieldInfos()) {
            if (oldSqlContainField(oldList, fieldInfo)) {
                oldIndex++;
                continue;
            }
            String fieldSql = genfieldSql(fieldInfo, response);
            oldList.add(oldIndex, fieldSql);
            oldIndex++;
        }
        return Lists.newArrayList(oldList);

    }

    private static boolean oldSqlContainField(@NotNull List<String> oldList, @NotNull PojoFieldInfo fieldInfo) {
        String keyWord = "`" + GenCodeUtil.getUnderScore(fieldInfo.getFieldName()) + "`";
        for (String s : oldList) {
            if (s.contains(keyWord)){
                return true;
            }
        }
        return false;
    }

    private static int findFirstFieldPos(@NotNull List<String> oldList) {
        int index = 0;
        for (String fieldSql : oldList) {
            if (StringUtils.isBlank(fieldSql)) {
                index++;
                continue;
            }
            if (GenCodeUtil.sqlContain(fieldSql, "DROP TABLE")
                    || GenCodeUtil.sqlContain(fieldSql, "CREATE TABLE")
                    || GenCodeUtil.sqlContain(fieldSql, "auto Generated on")
                    ) {
                index++;
                continue;
            }
            break;
        }
        return index;
    }

    private static String genfieldSql(@NotNull PojoFieldInfo fieldInfo, GenCodeResponse response) {
        StringBuilder ret = new StringBuilder();

        if (fieldInfo.getFieldName().equalsIgnoreCase("lastUpdate")) {
            ret.append(ONE_RETRACT)
                    .append("`last_update` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',");
            return ret.toString();
        }

        if (fieldInfo.getFieldName().equalsIgnoreCase("createTime")) {
            ret.append(ONE_RETRACT)
                    .append("`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',");
            return ret.toString();
        }

        if (fieldInfo.getFieldName().equals("id")) {
            ret.append(ONE_RETRACT).append("`id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',");
            return ret.toString();
        }

        String filedClassDefault = getDefaultField(fieldInfo, response);
        ret.append(ONE_RETRACT).append("`").append(GenCodeUtil.getUnderScore(fieldInfo.getFieldName())).append("` ")
                .append(filedClassDefault).append(" COMMENT '").append(getFieldComment(fieldInfo)).append("',");
        return ret.toString();
    }

    // todo
    private static String getFieldComment(PojoFieldInfo fieldInfo) {
        return StringUtils.EMPTY;
    }

    private static String getDefaultField(PojoFieldInfo fieldInfo, GenCodeResponse response) {
        Map<String, String> userConfigMap = response.getUserConfigMap();
        String key = fieldInfo.getFieldClass().toLowerCase();
        String value = userConfigMap.get(key);
        if (StringUtils.isBlank(value)) {
            if(StringUtils.equalsIgnoreCase(key,"String")){
                return "VARCHAR(50) NOT NULL DEFAULT ''";
            }else if(StringUtils.equalsIgnoreCase(key,"Integer")){
                return "INTEGER(12) NOT NULL DEFAULT -1";
            }else if(StringUtils.equalsIgnoreCase(key,"short")){
                return "TINYINT NOT NULL DEFAULT -1";
            }else if(StringUtils.equalsIgnoreCase(key,"date")){
                return "DATETIME NOT NULL DEFAULT '1000-01-01 00:00:00'";
            }else if(StringUtils.equalsIgnoreCase(key,"Long")){
                return "BIGINT NOT NULL DEFAULT -1";
            }else if(StringUtils.equalsIgnoreCase(key,"BigDecimal")){
                return "DECIMAL(14,4) NOT NULL DEFAULT 0";
            }else if(StringUtils.equalsIgnoreCase(key,"double")){
                return "DECIMAL(14,4) NOT NULL DEFAULT 0";
            }else if(StringUtils.equalsIgnoreCase(key,"float")){
                return "DECIMAL(14,4) NOT NULL DEFAULT 0";
            }else {
                throw new RuntimeException("error");
            }
        }
        return value;
    }

    public static void main(String[] args) {
        List<Integer> list = Lists.newArrayList();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(1, 3);
        System.out.println(list);
        System.out.println(list.subList(1, 4));
    }

}


