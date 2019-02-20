package com.gz.design.panda.biz.utils;

import com.caiyi.financial.nirvana.cash.loan.common.entity.EnumEntity;
import com.caiyi.financial.nirvana.cash.loan.common.enums.IBaseCodeEnum;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Linxingyu on 2018/8/2.
 */
public class EnumUtil {

    /**
     * 枚举转换list
     */
    public static <E extends Enum<?> & IBaseCodeEnum> List<EnumEntity> getEnumList(Class<E> enumT) {
        List<EnumEntity> list = new ArrayList<>();
        for (E e : enumT.getEnumConstants()) {
            list.add(new EnumEntity(e.getCode(), e.getDescription()));
        }
        return Collections.unmodifiableList(list);
    }

    /**
     * 根据枚举中部分返回list
     */
    public static <E extends Enum<?> & IBaseCodeEnum> List<EnumEntity> getEnumListInclude(
            Class<E> enumT, E var) {
        List<EnumEntity> list = new ArrayList<>();
        for (E e : enumT.getEnumConstants()) {
            if (var.getCode() == e.getCode()) {
                list.add(new EnumEntity(e.getCode(), e.getDescription()));
                break;
            }
        }
        return Collections.unmodifiableList(list);
    }

    /**
     * 排除枚举中部分
     */
    public static <E extends Enum<?> & IBaseCodeEnum> List<EnumEntity> getEnumListExclude(
            Class<E> enumT, E var) {
        List<EnumEntity> list = new ArrayList<>();
        for (E e : enumT.getEnumConstants()) {
            if (var.getCode() != e.getCode()) {
                list.add(new EnumEntity(e.getCode(), e.getDescription()));
            }
        }
        return Collections.unmodifiableList(list);
    }

    /**
     * 枚举转换map
     */
    public static <E extends Enum<?> & IBaseCodeEnum> Map getEnumMap(Class<E> enumT) {
        Map<String, String> map = new HashMap<>();
        for (E e : enumT.getEnumConstants()) {
            map.put(String.valueOf(e.getCode()), e.getDescription());
        }
        return Collections.unmodifiableMap(map);
    }

}
