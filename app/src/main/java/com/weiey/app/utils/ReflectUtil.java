package com.weiey.app.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 反射工具
 */
public class ReflectUtil {
    /**
     * 得到对象所有字段键值
     *
     * @param o
     * @return
     */
    public static Map<String, Object> reflect(Object o) {
        //获取参数类
        Class cls = o.getClass();
        //将参数类转换为对应属性数量的Field类型数组（即该类有多少个属性字段 N 转换后的数组长度即为 N）
        Field[] fields = cls.getDeclaredFields();
        Map<String, Object> fieldsMap = new HashMap<>();
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            f.setAccessible(true);
            try {
                // 属性名：applyReturnNote；属性值：退单审核备注;字段类型：class java.lang.String
                //f.getName()得到对应字段的属性名，f.get(o)得到对应字段属性值,f.getGenericType()得到对应字段的类型
//                LogUtil.d("DeclaredField ==>","属性名："+f.getName()+";属性值："+f.get(o)+";字段类型：" + f.getGenericType());


                String fieldName = f.getName();
                fieldsMap.put(fieldName, f.get(o));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return fieldsMap;
    }
}
