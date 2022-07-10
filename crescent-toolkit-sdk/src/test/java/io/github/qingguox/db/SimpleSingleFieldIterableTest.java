package io.github.qingguox.db;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * @author wangqingwei
 * Created on 2022-06-22
 */
public class SimpleSingleFieldIterableTest {

    private static final Logger logger = LoggerFactory.getLogger(SimpleSingleFieldIterableTest.class);

    @Test
    public void test() {
        int userCount = 100;
        // 用list模拟db
        List<UserInfo> userInfoList = Lists.newArrayList();
        for (int count = 0; count < userCount; count++) {
            userInfoList.add(new UserInfo(count, "1 + " + count));
        }
        List<UserInfo> result = Lists.newArrayListWithCapacity(16);
        new SimpleSingleFieldIterable<>(-1L, 10, (minId, count) -> getByMinId(userInfoList, minId, count),
                UserInfo::getUserId).forEach(result::addAll);
        System.out.println(result);
    }

    public List<UserInfo> getByMinId(List<UserInfo> userInfoList, long minId, int count) {
        logger.info("start minId : {}, count : {}", minId, count);
        List<UserInfo> result = Lists.newArrayList();
        AtomicInteger curCount = new AtomicInteger(count);
        userInfoList.forEach(cur -> {
            if (cur.getUserId() > minId && curCount.get() > 0) {
                result.add(cur);
                curCount.getAndDecrement();
            }
        });
        logger.info("end curCount : {}, result : {}", curCount.get(), result);
        return result;
    }

    public static class UserInfo {
        private long userId;
        private String name;

        public UserInfo() {
        }

        public UserInfo(long userId, String name) {
            this.userId = userId;
            this.name = name;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
        }
    }
}
