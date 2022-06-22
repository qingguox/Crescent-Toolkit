package io.github.qingguox.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author wangqingwei
 * Created on 2022-06-22
 */
public class ShardOperationUtils {

    /**
     * 对一个集合的数据 (集合中数据类型为M) 进行分组 (分组Key为K, 分组方法为shardKeyFunction)
     * 分组后每组按照batchSize数量, 使用operationFunction进行处理, 最终返回结果(类型为R)
     * K必须是可以map的key
     */
    public static <K, M, R> List<R> shardOperation(Collection<M> models, int batchSize, Function<M, K> shardKeyFunction,
            BiFunction<K, List<M>, List<R>> operationFunction) {
        if (CollectionUtils.isEmpty(models)) {
            return Collections.emptyList();
        }
        Map<K, List<M>> shardMap = Maps.newHashMap();
        models.forEach(model -> {
            K shardKey = shardKeyFunction.apply(model);
            List<M> modelList = shardMap.computeIfAbsent(shardKey, key -> new ArrayList<>());
            modelList.add(model);
        });
        List<R> resultList = new ArrayList<>(models.size());
        shardMap.forEach((shardKey, modelList) -> Lists.partition(modelList, batchSize).forEach(modelBatchList -> {
            List<R> batchResult = operationFunction.apply(shardKey, modelBatchList);
            resultList.addAll(batchResult);
        }));
        return resultList;
    }
}
