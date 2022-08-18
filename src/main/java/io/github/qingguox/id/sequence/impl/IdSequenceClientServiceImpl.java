package io.github.qingguox.id.sequence.impl;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import io.github.qingguox.id.sequence.IdRule;
import io.github.qingguox.id.sequence.IdSequenceClientService;
import io.github.qingguox.id.sequence.dao.IdGenDAO;
import io.github.qingguox.id.sequence.model.ClientIdCache;
import io.github.qingguox.id.sequence.model.IdGen;
import io.github.qingguox.json.JacksonUtils;

/**
 * @author wangqingwei
 * Created on 2022-08-18
 */
@Lazy
@Service
public class IdSequenceClientServiceImpl implements IdSequenceClientService {

    private static final Logger logger = LoggerFactory.getLogger(IdSequenceClientServiceImpl.class);

    /**
     * bizId, [1, 10) cur=1
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
        long startTimeMills = System.currentTimeMillis();
        IdGen idGen = idGenDAO.getByBizId(bizId);
        Assert.notNull(idGen, "idGen not exists! bizId : " + bizId);

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
    public IdRule supportRule() {
        return IdRule.SINGLE_SECTION;
    }

    private ClientIdCache genClientIdCache(long bizId) {
        // 极少意外情况, 比如当前线程被解锁了, 其他线程生成了一个
        ClientIdCache oldCache = idCache.getIfPresent(bizId);
        if (oldCache != null && oldCache.getCurId() < oldCache.getRightId()) {
            logger.info("getIdBy : {}", "OtherThreadGen");
            return oldCache;
        }

        IdGen idGen = idGenDAO.getByBizId(bizId);

        final long version = idGen.getVersion();
        final long maxId = idGen.getMaxId();
        final long id = idGen.getId();
        final int step = idGen.getStep();

        long nextMaxId = maxId + step;
        int updateCount = idGenDAO.updateByIdAndVersion(id, version, nextMaxId);
        // 其他进程已经修改了
        if (updateCount == 0) {
            logger.info("other processor is updated! so try once! bizId : {}, curIdVersion : {}, curIdNextMaxId : {}",
                    bizId, version, nextMaxId);
            return genClientIdCache(bizId);
        }
        final ClientIdCache
                clientIdCache = new ClientIdCache(maxId, maxId, nextMaxId);
        logger.info("genClientIdCache : {}, bizId : {}", JacksonUtils.toJSON(clientIdCache), bizId);
        return clientIdCache;
    }

    private long checkAndGet(long resultId, long startTimeMills) {
        Assert.isTrue(resultId != 0L, "resultId is 0L");
        final long endTimeMills = System.currentTimeMillis();
        logger.info("checkAndGet cost : {}", endTimeMills - startTimeMills);
        return resultId;
    }
}
