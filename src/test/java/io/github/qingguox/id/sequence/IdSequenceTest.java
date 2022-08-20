package io.github.qingguox.id.sequence;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * @author wangqingwei
 * Created on 2022-08-17
 */
@SpringBootApplication(scanBasePackages = "io.github")
public class IdSequenceTest implements CommandLineRunner {
    LinkedBlockingQueue queue = new LinkedBlockingQueue();
    ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 100,
            300, TimeUnit.SECONDS, queue, new ThreadFactoryBuilder() //
            .setNameFormat("data-load-executor-holder-%d") //
            .build());

    private static final Logger logger = LoggerFactory.getLogger(IdSequenceTest.class);

    @Autowired
    private IdSequenceService idSequenceService;

    public void testGenId(String idBizType) throws InterruptedException {
        long startTimeMills = System.currentTimeMillis();
        int count = 10000;
        final Thread thread = Thread.currentThread();
        Set<Long> idSet = Sets.newConcurrentHashSet();
        List<Future<?>> futureList = Lists.newArrayList();
        for (int cur = 1; cur <= count; cur++) {
            final Future<?> submit = executor.submit(() -> {
                final long id = idSequenceService.getId(idBizType);
                idSet.add(id);
                logger.info("gen id : {}", id);
            });
            futureList.add(submit);
        }
        if (CollectionUtils.isNotEmpty(futureList)) {
            futureList.stream().map(cur -> {
                try {
                    final Object oo = cur.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 1;
            }).collect(Collectors.toList());
            logger.info("main cost : {}", System.currentTimeMillis() - startTimeMills);
        }
        thread.join();
        Assert.isTrue(count == idSet.size(), "count != idSet.size");
    }

    @Override
    public void run(String... args) throws Exception {
        // 测试客户端批量单号段
//        testGenId("testGenIdSingleSection");
        // 测试客户端批量单号段
        testGenId("testGenIdTwoSection");
    }

    public static void main(String[] args) {
        SpringApplication.run(IdSequenceTest.class, args);
    }
}
