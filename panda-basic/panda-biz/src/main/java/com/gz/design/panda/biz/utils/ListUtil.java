package com.gz.design.panda.biz.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

/**
 * @Author wangqiang
 * @Date 2018/11/2 16:47
 **/
@Slf4j
public class ListUtil {

    //判断列表是否为空
    public static <T> boolean isEmpty(List<T> list){
        return  list == null || list.size() == 0;
    }

    //判断列表是否为非空
    public static <T> boolean isNotEmpty(List<T> list){
        return  list != null && list.size() > 0;
    }
    /**
     * 转换list工具
     * @param originList
     * @param eClass
     * @param <E>
     * @param <T>
     * @return
     */
    public static<E,T> List<E> entityListToModelList(List<T> originList,Class<E> eClass){
        if (null == originList || originList.isEmpty()) {
            return null;
        }
        List<E> eList = new ArrayList<E>();
        for (T entity : originList) {
            if (null == entity) {
                eList.add(null);
            }else {
                try {
                    Object model = eClass.newInstance();
                    BeanUtils.copyProperties(entity,model);
                    eList.add((E)model);
                } catch (Exception e) {
                    log.error("实例化异常",e);
                }

            }
        }
        return eList;
    }
    public static <T> List<T> popItemList(List<T> list, int size) {
        List<T> itemList = new ArrayList<>();
        int i = 1;
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            if (i > size) {
                break;
            }
            T item = it.next();
            itemList.add(item);
            it.remove();
            i++;
        }
        return itemList;
    }

}
