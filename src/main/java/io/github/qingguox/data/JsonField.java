package io.github.qingguox.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 配合AdvancedBeanPropertyRowMapper和AdvancedBeanPropertySqlParameterSource实现json和pojo的转换.
 * @author wangqingwei
 * Created on 2022-08-06
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonField {
}
