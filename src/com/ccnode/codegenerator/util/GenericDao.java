package com.ccnode.codegenerator.util;


import com.ccnode.codegenerator.pojo.DaoParam;

import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p/>
 * Created by zhengjun.du on 2016/01/03 22:20
 */
public interface GenericDao<T> {

    public int add( T pojo);

    public int adds( List<T> pojos);

    public List<T> query( T pojo,  Integer withLimit);

    public List<T> query( T pojo, DaoParam param);

    public List<T> query( T pojo, String queryCount , String addCondition, String limitCondition);

    public int update( T pojo,  String withLastUpdate);

    public int delete( T pojo);

}
