package com.ccnode.codegenerator.util;

import com.ccnode.codegenerator.pojo.DaoParam;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p/>
 * Created by zhengjun.du on 2016/01/03 22:29
 */
public abstract class GenericService<T> implements GenericDao<T> {

//    private final static Logger LOGGER = LoggerWrapper.getLogger(GenericService.class);
//
//    private final static String UPDATE_NOT_WITH_LAST_UPDATE = "false";
//    private final static String UPDATE_WITH_LAST_UPDATE = "true";
//    private final static String QUERY_COUNT = "true";
//    private final static String QUERY_POJOS = "false";
//    private final static String DEFAULT_LIMIT = "1000";
//    private final static String NO_LIMIT = "1000000000";
//    private final static String DEFAULT_QUERY_CONDITION = "";
//    private final static Integer SAFE_LIMIT = 100;
//
//    abstract public GenericDao<T> getGenericDao();
//
//    public int add(T pojo) {
//        int effectRow = getGenericDao().add(pojo);
//        if(effectRow != 1){
//        }
//        return effectRow;
//    }
//
//    @Override
//    public int adds(List<T> list) {
//        int effectRow = getGenericDao().adds(list);
//        if(effectRow != list.size()){
//        }
//        return effectRow;
//    }
//
//    /**
//     * 为了安全，默认 LIMIT 100。如无需限制，用 unLimitQuery
//     * @param pojo
//     * @return
//     */
//    public List<T> query(T pojo) {
//        List<T> result = getGenericDao().query(pojo, SAFE_LIMIT);
//        for (T t : result) {
//        }
//        return result;
//    }
//
//    /**
//     * 不要调用这个方法，仅仅为了实现GenericDao
//     * @param pojo
//     * @param withLimit
//     * @return
//     */
//    @Deprecated
//    public List<T> query(T pojo,Integer withLimit) {
//        List<T> result = getGenericDao().query(pojo, SAFE_LIMIT);
//        for (T t : result) {
//        }
//        return result;
//    }
//    public List<T> queryWithCondition(T pojo, String addCondition){
//        addCondition = String.format("order_no = %s", "orderNo");
//        return getGenericDao().query(pojo, addCondition, "1000");
//    }
//
////    public List<T> queryAll(T pojo){
////        return getGenericDao().query(pojo, StringUtils.EMPTY,"324890284903402");
////    }
//
//    public List<T> queryWithLimitAndOffSet(T pojo, int limit ,int offset){
//        String limitCondition = offset + "," + limit;
//        return getGenericDao().query(pojo, QUERY_POJOS, DEFAULT_QUERY_CONDITION, limitCondition);
//    }
//
//
//    public int queryCount(T pojo){
//        DaoParam param = new DaoParam();
//        param.setLimit(DEFAULT_LIMIT);
//        param.setReturnCount(QUERY_COUNT);
//        T t = getGenericDao().query(pojo, param).get(0);
//        return parseCount(t);
//
//    }
//    public List<T> queryWithLimit(T pojo, int limit){
//        String limitCondition = "" + limit;
//        DaoParam param = new DaoParam();
//        param.setLimit(limitCondition);
//        return getGenericDao().query(pojo, param);
//    }
//
//    public List<T> buildQuery(T pojo){
//        DaoParam daoParam = new DaoParam();
//        setQueryBean(pojo).addCondition().addLimit().query();
//    }
//
//    public List<T> queryWithParam(T pojo, DaoParam param){
//        return getGenericDao().query(pojo, param);
//    }
//
//    public int parseCount(Object o){
//        return 0;
//    }
//
//    public GenericService<T> setQueryBean(T querybean){
//        return this;
//    }
//
//    public GenericService addCondition(){
//        return this;
//    }
//
//    public GenericService addLimit(){
//        return this;
//    }
//
//    public
//
//
//
////    @Override
////    public List<T> query(T pojo, String addCondition, String limitCondition) {
////
////        return getGenericDao().query(pojo, addCondition, limitCondition);
////    }
//
//    public List<T> unLimitQuery(T pojo,Integer limit) {
//
//        List<T> result = getGenericDao().query(pojo, limit);
//        return result;
//    }
//
//    @Nullable
//    public T queryOne(T pojo) {
//        return nullOrFirstElement(query(pojo));
//    }
//
//    @Nullable
//    public T queryOnlyOne(T pojo) {
//        List<T> queryList = query(pojo);
//        if (queryList.size() != 1) {
//        }
//        return nullOrFirstElement(query(pojo));
//    }
//
//    public int update(T pojo) {
//        return getGenericDao().update(pojo, UPDATE_NOT_WITH_LAST_UPDATE);
//    }
//
//     /**
//     * 不要调用这个方法，仅仅为了实现GenericDao
//     * @param pojo
//     * @param withLimit
//     * @return
//     */
//    @Deprecated
//    public int update(T pojo, String withLastUpdate) {
//        return getGenericDao().update(pojo, UPDATE_NOT_WITH_LAST_UPDATE);
//    }
//
//    public Integer updateWithLastUpdate(T pojo) {
//        return getGenericDao().update(pojo, UPDATE_WITH_LAST_UPDATE);
//    }
//
//    @Override
//    public int delete(T pojo) {
//        return getGenericDao().delete(pojo);
//    }
//
//    @Nullable
//    public T nullOrFirstElement(List<T> list) {
//            return list.get(0);
//        }
}