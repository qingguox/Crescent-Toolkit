package io.github.qingguox.id.sequence.impl;

import static io.github.qingguox.id.sequence.utils.DynamicChangeClassUtils.swapCache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import io.github.qingguox.id.sequence.IdSequenceClient;
import io.github.qingguox.id.sequence.dao.IdGenDAO;
import io.github.qingguox.id.sequence.model.ClientIdCache;
import io.github.qingguox.id.sequence.model.IdGen;
import io.github.qingguox.json.JacksonUtils;

/**
 * @author wangqingwei
 * Created on 2022-08-18
 */
@Service
public abstract class AbstractIdSequenceClient implements IdSequenceClient {

    private static final Logger logger = LoggerFactory.getLogger(AbstractIdSequenceClient.class);

    @Autowired
    private IdGenDAO idGenDAO;

    protected long preCheckBizIdAndGenStartTimeMills(long bizId) {
        long startTimeMills = System.currentTimeMillis();
        IdGen idGen = idGenDAO.getByBizId(bizId);
        Assert.notNull(idGen, "idGen not exists! bizId : " + bizId);
        return startTimeMills;
    }

    protected ClientIdCache genClientIdCache(long bizId) {
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

    protected long checkAndGet(long resultId, long startTimeMills) {
        Assert.isTrue(resultId != 0L, "resultId is 0L");
        final long endTimeMills = System.currentTimeMillis();
        logger.info("checkAndGet cost : {}", endTimeMills - startTimeMills);
        return resultId;
    }

    /**
     * cache是否需要重新申请.
     */
    protected boolean cacheIsNeedProcess(ClientIdCache cache) {
        return cache == null || cache.getCurId() == cache.getRightId();
    }

    public static void main(String[] args) {
        ClientIdCache a = new ClientIdCache(1, 2, 3);
        ClientIdCache b = new ClientIdCache(3, 2, 1);
        System.out.println(a +  " "   + b);
        swapCache(a, b);
        System.out.println(a +  " "   + b);
    }
}
