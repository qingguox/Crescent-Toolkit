package io.github.qingguox.date;

import static io.github.qingguox.date.DateConvertUtils.*;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 * @author wangqingwei
 * Created on 2022-07-10
 */
public class DateConvertUtilsTest {

    @Test
    public void test() {
        System.out.println(getDateByTimeStamp(System.currentTimeMillis()));

        System.out.println(getDefaultDateByTimeStamp(System.currentTimeMillis()));

        System.out.println(getDateByTimeStampAndPattern(System.currentTimeMillis(), YYYYMMDD_DATE_FORMATE));

        System.out.println(getDayStartTime(System.currentTimeMillis()));

        System.out.println(diffDaysBetweenTwoTimeStamp(System.currentTimeMillis(), System.currentTimeMillis() + TimeUnit.DAYS.toMillis(2)));
    }

}
