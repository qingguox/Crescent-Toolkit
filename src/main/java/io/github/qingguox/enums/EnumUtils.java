package io.github.qingguox.enums;

import java.util.Arrays;

/**
 * @author wangqingwei
 * Created on 2022-08-05
 */
public class EnumUtils {

    public static <T extends Enum & IntDescValue> T fromValue(Class<T> clazz, int value) {
        return fromValue(clazz, value, null);
    }

    public static <T extends Enum & IntDescValue> T fromValue(Class<T> clazz, int value, T defaultValue) {
        return Arrays.stream(clazz.getEnumConstants())
                .filter(e -> e.getValue() == value)
                .findFirst().orElse(defaultValue);
    }

}
