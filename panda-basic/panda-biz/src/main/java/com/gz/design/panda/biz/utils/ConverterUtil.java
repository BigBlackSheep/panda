package com.gz.design.panda.biz.utils;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;

/**
 * @program: yofishdk-cash-loan-business
 * @description: 进行beanUtil数据转换
 * @author: WangYx
 * @create: 2018/08/15
 */
public class ConverterUtil {

    /**
     * 直接返回target实体类
     */
    public static <U, V> V copyBeanProperty(U sourceClazz, Class<V> targetClazz) {

        try {
            if (sourceClazz != null) {
                V targetInstance = targetClazz.newInstance();
                BeanUtils.copyProperties(sourceClazz, targetInstance);
                return targetInstance;
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回targetClazz的list
     * @param sourceList
     * @param targetClazz
     * @param <U>
     * @param <V>
     * @return
     */
    public static <U, V> List<V> copyListProperty(List<U> sourceList, Class<V> targetClazz) {

        if (sourceList != null && sourceList.size() > 0) {
            ArrayList<V> targetList = new ArrayList<>();
            for (U sourceClazz : sourceList) {
                targetList.add(copyBeanProperty(sourceClazz, targetClazz));
            }
            return targetList;
        }
        return null;
    }
}