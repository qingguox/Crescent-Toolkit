package io.github.qingguox.id.sequence.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;

import io.github.qingguox.id.sequence.IdRule;
import io.github.qingguox.id.sequence.IdSequenceClient;

/**
 * @author wangqingwei
 * Created on 2022-08-18
 */
@Component
public class IdSequenceServiceFactory {

    private static final Logger logger = LoggerFactory.getLogger(IdSequenceServiceFactory.class);
    private final Map<IdRule, IdSequenceClient> map = Maps.newConcurrentMap();

    @Autowired
    private IdSequenceServiceFactory(List<IdSequenceClient> clientServiceList) throws Exception {
        // 1. 检查是否为null
        clientServiceList = checkHasNull(clientServiceList);
        // 2. check是否有重复
        checkDuplicate(clientServiceList);
        // 3. put
        putAll(clientServiceList);
    }

    @Nonnull
    public IdSequenceClient getIdSequenceClientByRule(IdRule idRule) {
        return map.get(idRule);
    }

    private void putAll(List<IdSequenceClient> clientServiceList) {
        for (IdSequenceClient client : clientServiceList) {
            logger.info("[IdSequenceServiceFactory] rule={}, FQCN={}", client.supportRule(), client.getClass().getCanonicalName());
            map.put(client.supportRule(), client);
        }
    }

    private void checkDuplicate(List<IdSequenceClient> clientServiceList) throws Exception {
        if (CollectionUtils.isEmpty(clientServiceList)) {
            return;
        }

        ArrayListMultimap<IdRule, String> duplicateMap = ArrayListMultimap.create();
        clientServiceList.stream().collect(Collectors.groupingBy(IdSequenceClient::supportRule))
                .forEach((key, clients) -> {
                    if (clients.size() > 1) {
                        clients.forEach(client -> duplicateMap.put(client.supportRule(), client.getClass().getCanonicalName()));
                    }
                });

        if (duplicateMap.size() > 0) {
            throw new Exception("IdSequenceService single rule has multi implement ===>\r\n"
                    + duplicateMap);
        }
    }

    private List<IdSequenceClient> checkHasNull(List<IdSequenceClient> clientServiceList) {
        logger.info("[IdSequenceServiceFactory] clientService size = {}", clientServiceList.size());
        return clientServiceList.stream().filter(client -> {
            boolean isNull = client.supportRule() == null;
            if (isNull) {
                logger.error("[IdSequenceServiceFactory] null rule : {}",
                        client.getClass().getCanonicalName());
            }
            return !isNull;
        }).collect(Collectors.toList());
    }

}
