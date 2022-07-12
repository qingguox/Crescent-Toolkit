package io.github.qingguox.db;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import io.github.qingguox.db.SimpleSingleFieldIterableTest.UserInfo;

/**
 * @author wangqingwei
 * Created on 2022-06-22
 */
public class ShardOperationUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(SimpleSingleFieldIterableTest.class);

    @Test
    public void test() {
        int userCount = 100;
        // 用list模拟db
        List<UserInfo> userInfoList = Lists.newArrayList();
        for (int count = 0; count < userCount; count++) {
            userInfoList.add(new UserInfo(count, "1 + " + count));
        }
        List<UserInfo> result = ShardOperationUtils.shardOperation(userInfoList, 10, this::getShardKey, this::operationKeyAndModelList);
        logger.info("out : {}", result);
    }

    private List<UserInfo> operationKeyAndModelList(long shardKey, List<UserInfo> userInfos) {
        logger.info("shardKey : {}, userInfos : {}", shardKey, userInfos);
        userInfos.forEach(cur -> cur.setName(cur.getName() + " + change"));
        return userInfos;
    }

    private long getShardKey(UserInfo userInfo) {
        return userInfo.getUserId() % 10;
    }
}
