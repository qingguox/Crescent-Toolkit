package io.github.qingguox.data;

import static io.github.qingguox.json.JacksonUtils.toJSON;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import io.github.qingguox.enums.IntValue;

/**
 * 加强版BeanPropertySqlParameterSource 将注解了@JsonField的属性序列化为json字符串 将枚举序列化为int
 * 序列化方式: Jackson升级版
 * @author wangqingwei
 * Created on 2022-08-06
 */
public class AdvancedBeanPropertySqlParameterSource extends BeanPropertySqlParameterSource {

    private final Set<String> jsonFields;
    private MapSqlParameterSource mapSqlParameterSource;


    public AdvancedBeanPropertySqlParameterSource(Object object) {
        this(object, null);
    }

    public AdvancedBeanPropertySqlParameterSource(Object object, Set<String> jsonFields) {
        super(object);
        // 查找JsonField注解, 记录哪些属性值需要json转化
        final Set<String> fields = new HashSet<>();
        final Class<?> mappedClass = object.getClass();
        FieldUtils.getFieldsListWithAnnotation(mappedClass, JsonField.class)
                .forEach(field -> fields.add(field.getName()));
        if (CollectionUtils.isNotEmpty(jsonFields)) {
            fields.addAll(jsonFields);
        }
        this.jsonFields = fields;
    }

    @Override
    public Object getValue(String paramName) throws IllegalArgumentException {
        // 优先从map里读
        if (mapSqlParameterSource != null && mapSqlParameterSource.hasValue(paramName)) {
            return mapSqlParameterSource.getValue(paramName);
        }

        Object value = super.getValue(paramName);
        if (jsonFields.contains(paramName)) {
            return value == null ? null : toJSON(value);
        }
        if (value instanceof IntValue && value.getClass().isEnum()) {
            return ((IntValue) value).getValue();
        }
        return value;
    }

    public AdvancedBeanPropertySqlParameterSource addValue(String paramName, @Nullable Object value) {
        mapSqlParameterSource().addValue(paramName, value);
        return this;
    }

    /**
     * lazy load
     */
    public MapSqlParameterSource mapSqlParameterSource() {
        if (mapSqlParameterSource == null) {
            mapSqlParameterSource = new MapSqlParameterSource();
        }
        return mapSqlParameterSource;
    }

}
