package io.github.qingguox.id.sequence.impl;

import static io.github.qingguox.id.sequence.utils.DynamicChangeClassUtils.swapCache;
import static io.github.qingguox.money.NumberUtils.DEFAULT_SCALE;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import io.github.qingguox.id.sequence.IdRule;
import io.github.qingguox.id.sequence.model.ClientIdCache;
import io.github.qingguox.json.JacksonUtils;

/**
 * id生成器-客户端批量双号段实现.
 * 性能: 这样比单号段性能好一些, 当第一个号段快没数据的时候, 异步加载第二个号段数据.
 * 调用 io.github.qingguox.id.sequence.IdSequenceTest#testGenId(java.lang.String)
 * testGenId("testGenIdTwoSection");
 * Db: 表id_gen
 * INSERT INTO test.id_gen (id, max_id, step, version, biz_id) VALUES (2, 1, 10, 1, 2);
 *
 * @author wangqingwei
 * Created on 2022-08-18
 */
@Lazy
@Service
public class IdSequenceTwoSectionClient extends AbstractIdSequenceClient {

    private static final Logger logger = LoggerFactory.getLogger(IdSequenceTwoSectionClient.class);
    private static final double PERCENTAGE = 0.8d;

    /**
     * bizId, [1, 11) cur=5
     * AtomicReference
     */
    private final Cache<Long, ClientIdCache> mainCache = CacheBuilder.newBuilder()
            .maximumSize(500)
            .expireAfterWrite(2, TimeUnit.MINUTES)
            .build();
    /**
     * bizId, [11, 21) cur=10
     */
    private final Cache<Long, ClientIdCache> slaveCache = CacheBuilder.newBuilder()
            .maximumSize(500)
            .expireAfterWrite(2, TimeUnit.MINUTES)
            .build();

    private final Object LOCK = new Object();

    @Override
    public long getId(long bizId) {
        final long startTimeMills = preCheckBizIdAndGenStartTimeMills(bizId);

        synchronized (LOCK) {
            long resultId;
            ClientIdCache clientIdCache = mainCache.getIfPresent(bizId);
            ClientIdCache slaveIdCache = slaveCache.getIfPresent(bizId);
            logger.info("clientIdCache : {}, slaveIdCache : {}", clientIdCache, slaveIdCache);
            if (cacheIsNeedProcess(clientIdCache)) {
                if (cacheIsNeedProcess(slaveIdCache)) {
                    clientIdCache = genClientIdCache(bizId);
                    resultId = clientIdCache.getCurId();
                    clientIdCache.setCurId(resultId + 1);
                    mainCache.put(bizId, clientIdCache);
                    logger.info("getIdBy : {}", "Db");
                    return checkAndGet(resultId, startTimeMills);
                }
                // 交换
                swapCache(clientIdCache, slaveIdCache);
                resultId = clientIdCache.getCurId();
                clientIdCache.setCurId(resultId + 1);
                logger.info("getIdBy : {}", "SlaveCache");
                // 清理
                slaveCache.invalidate(bizId);
                return checkAndGet(resultId, startTimeMills);
            }
            final long curId = clientIdCache.getCurId();
            if (curId < clientIdCache.getRightId()) {
                resultId = curId;
                clientIdCache.setCurId(resultId + 1);
                logger.info("getIdBy : {}", "Cache");
                // checkMainCacheCapacityAndGen();
                checkMainCacheCapacityAndGen(clientIdCache, bizId);
                return checkAndGet(resultId, startTimeMills);
            }
        }
        return checkAndGet(0L, startTimeMills);
    }

    private void checkMainCacheCapacityAndGen(ClientIdCache mainCache, long bizId) {
        final long curId = mainCache.getCurId();
        final long rightId = mainCache.getRightId();
        final long leftId = mainCache.getLeftId();
        BigDecimal curPercentage = new BigDecimal(curId - leftId)
                .divide(BigDecimal.valueOf(rightId - leftId), DEFAULT_SCALE, RoundingMode.DOWN);
        logger.info("curPercentage : {}", curPercentage);
        if (Double.compare(curPercentage.doubleValue(), PERCENTAGE) >= 0) {
            // 看第二个是否有, 没有的话需要设置
            ClientIdCache curSlaveCache = slaveCache.getIfPresent(bizId);
            if (cacheIsNeedProcess(curSlaveCache)) {
                curSlaveCache = genClientIdCache(bizId);
                slaveCache.put(bizId, curSlaveCache);
                logger.info("loadingSlaveCache curSlaveCache : {}, bizId : {}", JacksonUtils.toJSON(curSlaveCache), bizId);
            }
        }
    }

    @Override
    public IdRule supportRule() {
        return IdRule.TWO_SECTION;
    }
}
