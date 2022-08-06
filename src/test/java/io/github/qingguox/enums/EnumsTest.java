package io.github.qingguox.enums;

/**
 * 枚举测试
 * @author wangqingwei
 * Created on 2022-08-05
 */
public enum EnumsTest implements IntDescValue {

    UNKNOWN(0, "未知"),
    FIRST(1, "第一个"),

    ;

    EnumsTest(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    private final int value;
    private final String desc;

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public int getValue() {
        return value;
    }

    public static EnumsTest fromValue(int value) {
        return EnumUtils.fromValue(EnumsTest.class, value, UNKNOWN);
    }
}
