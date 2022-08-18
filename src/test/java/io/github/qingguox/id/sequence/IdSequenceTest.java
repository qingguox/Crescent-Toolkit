package io.github.qingguox.id.sequence;

import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.Assert;

import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * @author wangqingwei
 * Created on 2022-08-17
 */
@SpringBootApplication(scanBasePackages = "io.github")
public class IdSequenceTest implements CommandLineRunner {
    LinkedBlockingQueue queue = new LinkedBlockingQueue();
    ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 15,
            300, TimeUnit.SECONDS, queue, new ThreadFactoryBuilder() //
            .setNameFormat("data-load-executor-holder-%d") //
            .build());

    private static final Logger logger = LoggerFactory.getLogger(IdSequenceTest.class);

    @Autowired
    private IdSequenceService idSequenceService;

    public void testGenId() throws InterruptedException {
        int count = 1000;
        Set<Long> idSet = Sets.newConcurrentHashSet();
        for (int cur = 1; cur <= count; cur++) {
            executor.submit(() -> {
                final long id = idSequenceService.getId("testGenIdSingleSection");
                idSet.add(id);
                logger.info("gen id : {}", id);
            });
        }
        Thread.sleep(10000);
        Assert.isTrue(count == idSet.size(), "count != idSet.size");
    }

    @Override
    public void run(String... args) throws Exception {
        testGenId();
    }

    public static void main(String[] args) {
        SpringApplication.run(IdSequenceTest.class, args);
    }
}
