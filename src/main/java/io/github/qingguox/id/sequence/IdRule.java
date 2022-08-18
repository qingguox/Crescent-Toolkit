package io.github.qingguox.id.sequence;

import io.github.qingguox.enums.EnumUtils;
import io.github.qingguox.enums.IntDescValue;

/**
 * @author wangqingwei
 * Created on 2022-08-18
 */
public enum IdRule implements IntDescValue {

    UNKNOWN(0, "未知"),
    SINGLE_SECTION(1, "单号段"),
    TWO_SECTION(2, "双号段")
    ;

    private final int value;
    private final String desc;

    IdRule(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public int getValue() {
        return value;
    }

    public static IdRule fromValue(int value) {
        return EnumUtils.fromValue(IdRule.class, value, UNKNOWN);
    }
}
