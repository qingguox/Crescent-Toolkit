package io.github.qingguox.id.sequence.impl;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import io.github.qingguox.id.sequence.IdRule;
import io.github.qingguox.id.sequence.dao.IdGenDAO;
import io.github.qingguox.id.sequence.model.ClientIdCache;

/**
 * id生成器-客户端批量单号段实现.
 * 调用 io.github.qingguox.id.sequence.IdSequenceTest#testGenId(java.lang.String)
 *     testGenId("testGenIdSingleSection");
 * Db: 表id_gen
 * INSERT INTO test.id_gen (id, max_id, step, version, biz_id) VALUES (1, 1, 10, 1, 1);
 * @author wangqingwei
 * Created on 2022-08-18
 */
@Lazy
@Service
public class IdSequenceSingleSectionClient extends AbstractIdSequenceClient {

    private static final Logger logger = LoggerFactory.getLogger(IdSequenceSingleSectionClient.class);

    /**
     * bizId, [1, 11) cur=1
     * AtomicReference
     */
    private final Cache<Long, ClientIdCache> idCache = CacheBuilder.newBuilder()
            .maximumSize(500)
            .expireAfterWrite(2, TimeUnit.MINUTES)
            .build();

    private final Object LOCK = new Object();

    @Autowired
    private IdGenDAO idGenDAO;

    @Override
    public long getId(long bizId) {
        final long startTimeMills = preCheckBizIdAndGenStartTimeMills(bizId);

        synchronized (LOCK) {
            long resultId;
            ClientIdCache clientIdCache = idCache.getIfPresent(bizId);
            if (clientIdCache == null || clientIdCache.getCurId() == clientIdCache.getRightId()) {
                clientIdCache = genClientIdCache(bizId);
                resultId = clientIdCache.getCurId();
                clientIdCache.setCurId(resultId + 1);
                idCache.put(bizId, clientIdCache);
                logger.info("getIdBy : {}", "Db");
                return checkAndGet(resultId, startTimeMills);
            }
            final long curId = clientIdCache.getCurId();
            if (curId < clientIdCache.getRightId()) {
                resultId = curId;
                clientIdCache.setCurId(resultId + 1);
                logger.info("getIdBy : {}", "Cache");
                return checkAndGet(resultId, startTimeMills);
            }
        }
        return checkAndGet(0L, startTimeMills);
    }

    @Override
    protected ClientIdCache genClientIdCache(long bizId) {
        // 极少意外情况, 比如当前线程被解锁了, 其他线程生成了一个
        ClientIdCache oldCache = idCache.getIfPresent(bizId);
        if (oldCache != null && oldCache.getCurId() < oldCache.getRightId()) {
            logger.info("getIdBy : {}", "OtherThreadGen");
            return oldCache;
        }
        return super.genClientIdCache(bizId);
    }

    @Override
    public IdRule supportRule() {
        return IdRule.SINGLE_SECTION;
    }
}
