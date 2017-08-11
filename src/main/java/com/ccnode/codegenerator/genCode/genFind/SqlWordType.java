package com.ccnode.codegenerator.genCode.genFind;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2017/08/04 22:47
 */
public enum SqlWordType {
    Select(0, "by,field,first,top"),
    Find(0, "by,field,first,top"),
    Count(0, "by"),
    Update(0, ""),
    Top(0, "number,by"),
    First(0, "number,by"),
    By(0, "field"),
    And(0, "field"),
    Or(0, "field"),
    Field(0, "between,by,or,and,like,StartWith,EndWith,not,in,greaterThan,greaterThanEqual,lessThan,lessThanEqual"),
    Number(0, "By"),
    Not(0, "like,in"),
    Between(0, "by,or,and"),
    In(0, "by,or,and"),
    Like(0, "by,or,and"),
    StartWith(0, "by,or,and"),
    EndWith(0, "by,or,and"),
    GreaterThanEqual(0, "by,or,and"),
    GreaterThan(0, "by,or,and"),
    LessThanEqual(0, "by,or,and"),
    LessThan(0, "by,or,and"),
    Before(0, "by,or,and"),
    After(0, "by,or,and"),
    Limit(0, "number"),
    OrderBy(0, "field"),
    Desc(0, ""),
    None(-1, "none");

    private Integer code;
    private String desc;

    public static Set<SqlWordType> CONDITION_JOINER_SET = ImmutableSet.of(Or, And, By, OrderBy);
    public static Set<SqlWordType> KEY_WORD_WITH_NUMBER_SET = ImmutableSet.of(Limit);
    public static Set<SqlWordType> KEY_WORD_WITH_FIELD_SET = ImmutableSet
            .of(By, And, Or, Between, In, GreaterThan, LessThan, Like, OrderBy);

    private SqlWordType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static SqlWordType fromNameIgnoreCase(String name) {
        if (StringUtils.isBlank(name)) {
            return SqlWordType.None;
        }
        for (SqlWordType e : SqlWordType.values()) {
            if (StringUtils.equalsIgnoreCase(name, e.name()) || StringUtils
                    .equalsIgnoreCase(name.replace("_", ""), e.name().replace("_", ""))) {
                return e;
            }
        }
        return SqlWordType.None;
    }

    public static SqlWordType fromCode(Integer code) {
        for (SqlWordType e : SqlWordType.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return SqlWordType.None;
    }

    public static SqlWordType fromDesc(String desc) {
        for (SqlWordType e : SqlWordType.values()) {
            if (e.getDesc().equals(desc)) {
                return e;
            }
        }
        return SqlWordType.None;
    }

}
