package io.github.qingguox.id.sequence.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.util.Assert;

/**
 * @author wangqingwei
 * Created on 2022-08-09
 */
public class DynamicChangeClassUtils {

    /**
     * 动态的设置对象的字段代表的值
     * 适用于: 知道对象内的fieldName, 但是不想手动set.
     *
     * @param obj 对象
     * @param clazz 对象的类Clazz
     * @param fieldName 字段名
     * @param newValue 字段值
     */
    public static <T> void dynamicChangeObjFieldValue(final T obj, Class<?> clazz, final String fieldName,
            Object newValue) {
        Assert.notNull(obj, "obj is null");
        Assert.hasLength(fieldName, "fieldName is blank");
        Assert.notNull(newValue, "newValue is null");
        try {
            if (clazz == null) {
                clazz = obj.getClass();
            }
            AtomicBoolean hasField = new AtomicBoolean(false);
            dynamicChangeObjFieldValueCommon(obj, clazz, fieldName, newValue, hasField);
            if (!hasField.get()) {
                throw new NoSuchFieldException(fieldName + " not existed! ");
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param obj 对象
     * @param clazz 类class
     * @param fieldName 字段名
     * @param newValue 新的字段值
     * @param hasField 是否已经处理子弹
     * @param <T> 泛型
     * @throws IllegalAccessException
     */
    public static <T> void dynamicChangeObjFieldValueCommon(final T obj, Class<?> clazz, final String fieldName,
            Object newValue,
            AtomicBoolean hasField) throws IllegalAccessException {
        if (clazz == null) {
            return;
        }
        if (hasField.get()) {
            return;
        }
        System.out.println("clazzName : " + clazz.getName());
        final Field[] declaredFields = clazz.getDeclaredFields();
        final Field field =
                Arrays.stream(declaredFields).filter(curField -> curField.getName().equals(fieldName))
                        .findFirst().orElse(null);
        if (field != null) {
            field.setAccessible(true);
            field.set(obj, newValue);
            field.setAccessible(false);
            hasField.set(true);
            return;
        }
        dynamicChangeObjFieldValueCommon(obj, clazz.getSuperclass(), fieldName, newValue, hasField);
    }

    /**
     * 只支持单个类, 无继承和基础类型等等
     * 后续可以优化.
     * 也可以搞个注解.
     * @param t1 对象1
     * @param t2 对象2
     * @param <T> 类型
     */
    public static <T> void swapCache(T t1, T t2) {
        changeField(t1, t2);
        changeField(t2, t1);
    }

    /**
     * 修改类中属性值
     * @param c1 对象1
     * @param c2 对象2
     * @param <T> 类型
     */
    public static <T> void changeField(T c1, T c2) {
        final Map<String, Field> t2FiledNameAndSelfMap =
                Arrays.stream(c2.getClass().getDeclaredFields())
                        .collect(Collectors.toMap(Field::getName, Function.identity()));
        Arrays.stream(c1.getClass().getDeclaredFields()).map(Field::getName).forEach(fieldName -> {
            final Field field = t2FiledNameAndSelfMap.get(fieldName);
            field.setAccessible(true);
            Object obj;
            try {
                obj = field.get(c2);
                field.setAccessible(false);
                dynamicChangeObjFieldValue(c1, c1.getClass(), fieldName, obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }
}
