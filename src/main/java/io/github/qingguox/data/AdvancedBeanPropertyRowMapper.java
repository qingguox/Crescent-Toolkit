package io.github.qingguox.data;

import static io.github.qingguox.json.JacksonUtils.fromJSON;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.fasterxml.jackson.databind.type.TypeFactory;

import io.github.qingguox.enums.IntValue;

/**
 * 加强版BeanPropertyRowMapper 将json字符串字段(通过添加注释@JsonField)反序列化java对象 支持将int字段
 * 反序列化为枚举
 * @author wangqingwei
 * Created on 2022-08-06
 */
public class AdvancedBeanPropertyRowMapper<T> extends BeanPropertyRowMapper<T> {

    private final Set<String> jsonFields;

    public AdvancedBeanPropertyRowMapper(Class<T> mappedClass) {
        this(mappedClass, null);
    }

    public AdvancedBeanPropertyRowMapper(Class<T> mappedClass, Set<String> jsonFields) {
        super(mappedClass);
        final Set<String> fields = new HashSet<>();
        FieldUtils.getFieldsListWithAnnotation(mappedClass, JsonField.class)
                .forEach(field -> fields.add(field.getName()));
        if (CollectionUtils.isNotEmpty(jsonFields)) {
            fields.addAll(jsonFields);
        }
        this.jsonFields = fields;
    }

    @Override
    protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
        if (jsonFields.contains(pd.getName())) {
            // 以json字符串反序列化为对象
            final Method readMethod = pd.getReadMethod();
            Type type = readMethod != null ? readMethod.getGenericReturnType()
                        : pd.getWriteMethod().getGenericParameterTypes()[0];
            String value = rs.getString(index);
            if (value == null) {
                return null;
            }

            return fromJSON(value, TypeFactory.defaultInstance().constructType(type));
        }

        final Class<?> requiredType = pd.getPropertyType();
        if (requiredType.isEnum() && IntValue.class.isAssignableFrom(requiredType)) {
            // 枚举实现了IntValue, 则以int值反序列化为枚举值
            final int enumValue = rs.getInt(index);
            return Arrays.stream(requiredType.getEnumConstants())
                    .filter(e -> Objects.equals(((IntValue) e).getValue(), enumValue))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(enumValue + " is invalid enum value!"));
        }

        return super.getColumnValue(rs, index, pd);
    }
}
