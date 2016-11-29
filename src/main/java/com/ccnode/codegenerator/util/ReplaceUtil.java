package com.ccnode.codegenerator.util;

import com.ccnode.codegenerator.function.EqualCondition;
import com.ccnode.codegenerator.pojo.ListInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/29 15:20
 */
public class ReplaceUtil {

    public static <T> Pair<Integer,Integer>  getPos(List<T> oldList, T start, T end, EqualCondition condition){
        int startPos = -1;
        int endPos = -1;
        int index  = 0;
        for (T t : oldList) {
            if(startPos == -1 && condition.isEqual(t, start)){
                startPos = index;
            }
            if(startPos >= 0 && condition.isEqual(t, end)){
                endPos = index + 1;
                return Pair.of(startPos,endPos);
            }
            index ++;
        }
        return Pair.of(oldList.size() - 1,oldList.size() - 1);
    }

    public static <T> void merge(ListInfo<T> listInfo, EqualCondition condition) {
        List<T> newSegments = listInfo.getNewSegments();
        List<T> selectSegments = listInfo.getSelectSegments();

        if(selectSegments == null || selectSegments.isEmpty()){
            listInfo.getFullList().addAll(newSegments);
            return;
        }

        List<T> replacedList = Lists.newArrayList();
        int index = 0;
        for (T t : newSegments) {
            if(index == 0){
                replacedList.add(selectSegments.get(0));
                index ++;
                continue;
            }
            if(index == newSegments.size() -1){
                replacedList.add(selectSegments.get(selectSegments.size() -1));
                index ++;
                continue;
            }
            T same = getSameFromOldList(selectSegments, t, condition);

            if(same != null){
                replacedList.add(same);
            }else{
                replacedList.add(t);
            }
            index ++;
        }
        List<T> newList = Lists.newArrayList();
        newList.addAll(listInfo.getFullList().subList(0,listInfo.getStartPos()));
        newList.addAll(replacedList);
        newList.addAll(listInfo.getFullList().subList(listInfo.getEndPos(),listInfo.getFullList().size()));
        listInfo.setFullList(newList);
    }

    @Nullable
    private static <T> T getSameFromOldList(List<T> selectList, T t, EqualCondition condition) {
        selectList = PojoUtil.avoidEmptyList(selectList);
        for (T element : selectList) {
            if(condition.isEqual(element, t)){
                return element;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(StringUtils.deleteWhitespace("sdfsk\""));
    }
}
