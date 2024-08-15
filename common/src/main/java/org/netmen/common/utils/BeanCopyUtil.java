package org.netmen.common.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtil {
    private BeanCopyUtil(){}   //私有化构造方法
    public static <V> V copyBean(Object source, Class<V> vClass){
        V result = null;
        try {
            result = vClass.newInstance();
            //BeanUtils是实现的基础的拷贝
            BeanUtils.copyProperties(source, result);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    //封装list集合的拷贝
    public static <O, V> List<V> copyBeanList(List<O> oList, Class<V> vClass){
        return oList.stream()
                .map(o -> copyBean(o, vClass))
                .collect(Collectors.toList());
    }
}
