package io.github.qingguox.money;

import java.math.RoundingMode;

import org.junit.Test;

/**
 * @author wangqingwei
 * Created on 2022-07-10
 */
public class NumberUtilsTest {

    private long fen = 1100;
    private String fenStr = String.valueOf(fen);

    @Test
    public void test() {
        testFenToYuan();
        testFenToSpecialMoney();
    }

    @Test
    public void testFenToYuan() {
        System.out.println(NumberUtils.fenToYuan(fen));
        System.out.println(NumberUtils.fenToYuan(fenStr));
        System.out.println(NumberUtils.fenToYuan(fen, 3, RoundingMode.DOWN));
        System.out.println(NumberUtils.fenToYuan(fenStr, 3, RoundingMode.DOWN));
    }

    @Test
    public void testFenToSpecialMoney() {
        System.out.println(NumberUtils.fenToSpecialMoney(fenStr, 1000, 2, RoundingMode.DOWN));
    }
}
