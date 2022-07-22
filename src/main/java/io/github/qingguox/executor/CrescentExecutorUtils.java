package io.github.qingguox.executor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangqingwei
 * Created on 2022-07-22
 */
public class CrescentExecutorUtils {

    private static final Logger logger = LoggerFactory.getLogger(CrescentExecutorUtils.class);

    /**
     * 解决问题:
     * <p>
     * 给定一批数据, 并且根据一定的规则多线程处理, 并对返回结果进行校验, 返回正确的结果.
     * </p>
     *
     * @param inputs 所需要处理的数据集
     * @param loadFunction 规则
     * @param predicate 校验方法
     * @param businessName 业务标记
     * @param timeout 单个任务获取超时时间
     * @param timeUnitLevel 单个任务获取超时级别
     * @param executorService 线程池
     */
    public static <T, O> List<O> parallelProcess(Collection<T> inputs, Function<T, O> loadFunction,
            Predicate<O> predicate, String businessName, long timeout, TimeUnit timeUnitLevel,
            ExecutorService executorService) {
        if (CollectionUtils.isEmpty(inputs) || loadFunction == null) {
            return Collections.emptyList();
        }

        List<Future<O>> tasks = inputs.stream().map(i -> executorService.submit(() -> loadFunction.apply(i)))
                .collect(Collectors.toList());

        List<O> results = new ArrayList<>(tasks.size());
        tasks.forEach(task -> {
            try {
                O o = task.get(timeout, timeUnitLevel);
                if (predicate.test(o)) {
                    results.add(o);
                } else {
                    logger.info("loadData fail! filter businessName : {}, output : {}", businessName, o);
                }
            } catch (Exception e) {
                logger.error("Data get Timeout! businessName : {}, data : {}", businessName, inputs, e);
            }
        });
        return results;
    }
}
