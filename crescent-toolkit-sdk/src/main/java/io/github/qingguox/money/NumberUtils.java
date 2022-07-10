package io.github.qingguox.money;

import static io.github.qingguox.constants.NumberConstants.ONE_HUNDRED;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangqingwei
 * Created on 2022-07-10
 */
public class NumberUtils {

    private static final Logger logger = LoggerFactory.getLogger(NumberUtils.class);

    public static final int DEFAULT_SCALE = 2;


    public static BigDecimal fenToYuan(long fen) {
        return fen > 0 ? new BigDecimal(fen).divide(BigDecimal.valueOf(ONE_HUNDRED), DEFAULT_SCALE, RoundingMode.DOWN)
                       : BigDecimal.valueOf(0);
    }

    /**
     * 分转元, 需要指定精度和取值
     * @param fen
     * @param scale
     * @param mode
     * @return
     */
    public static BigDecimal fenToYuan(long fen, int scale, RoundingMode mode) {
        return fen > 0 ? new BigDecimal(fen).divide(BigDecimal.valueOf(ONE_HUNDRED), scale, mode)
                       : BigDecimal.valueOf(0);
    }


    public static BigDecimal fenToYuan(String fen) {
        try {
            return new BigDecimal(fen).divide(BigDecimal.valueOf(ONE_HUNDRED), DEFAULT_SCALE, RoundingMode.DOWN);
        } catch (NumberFormatException e) {
            logger.error("Invalid fen! fen : {}, Exception : {}", fen, e);
            return BigDecimal.valueOf(0);
        }
    }

    /**
     * 分转元, 需要指定精度和取值
     * @param fen
     * @param scale
     * @param mode
     * @return
     */
    public static BigDecimal fenToYuan(String fen, int scale, RoundingMode mode) {
        try {
            return new BigDecimal(fen).divide(BigDecimal.valueOf(ONE_HUNDRED), scale, mode);
        } catch (NumberFormatException e) {
            logger.error("Invalid fen! fen : {}, Exception : {}", fen, e);
            return BigDecimal.valueOf(0);
        }
    }

    /**
     * 分转任意钱
     * @param fen
     * @param divideNum
     * @param scale
     * @param mode
     * @return
     */
    public static BigDecimal fenToSpecialMoney(String fen, long divideNum, int scale, RoundingMode mode) {
        try {
            return new BigDecimal(fen).divide(BigDecimal.valueOf(divideNum), scale, mode);
        } catch (NumberFormatException e) {
            logger.error("Invalid fen! fen : {}, Exception : {}", fen, e);
            return BigDecimal.valueOf(0);
        }
    }
}
