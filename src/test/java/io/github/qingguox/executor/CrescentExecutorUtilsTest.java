package io.github.qingguox.executor;

import static com.github.phantomthief.util.MoreSuppliers.lazy;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import com.github.phantomthief.util.MoreSuppliers;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * @author wangqingwei
 * Created on 2022-07-22
 */
public class CrescentExecutorUtilsTest {

    public static void main(String[] args) {
        int inputSize = 100;
        List<Integer> inputs = Lists.newArrayList();
        Stream.iterate(0, seed -> seed + 1).limit(inputSize).forEach(inputs::add);
        System.out.println(inputs);

        CrescentExecutorUtils
                .parallelProcess(inputs, CrescentExecutorUtilsTest::get, x -> x == 1,
                        "111", 100, SECONDS, getSyncDataToHiveExecutor());
    }

    private static int get(int input) {
        System.out.println(input);
        return 1;
    }

    private static final int DATA_KEEP_ALIVE_TIMEOUT = 30;
    private static final int DATA_CORE_THREAD = 2;
    private static final int DATA_MAX_THREAD = 4;
    private static final int DATA_QUEUE_SIZE = 500;
    private static final MoreSuppliers.CloseableSupplier<ExecutorService> DATA_THREAD_POOL_EXECUTOR;
    private static final AtomicReference<LinkedBlockingQueue<Runnable>> DATA_CONSUMER_QUEUE =
            new AtomicReference<>();

    static {
        DATA_THREAD_POOL_EXECUTOR = lazy(() -> {
            LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(DATA_QUEUE_SIZE);
            DATA_CONSUMER_QUEUE.set(queue);

            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(DATA_CORE_THREAD,
                    DATA_MAX_THREAD,
                    DATA_KEEP_ALIVE_TIMEOUT, SECONDS,
                    queue, new ThreadFactoryBuilder()
                    .setNameFormat("data-executor-holder-%d")
                    .build());
            threadPoolExecutor.allowCoreThreadTimeOut(true);
            return threadPoolExecutor;
        });
        Runtime.getRuntime().addShutdownHook(new Thread(() -> DATA_THREAD_POOL_EXECUTOR
                .tryClose(threadPool -> MoreExecutors.shutdownAndAwaitTermination(threadPool, 1, TimeUnit.DAYS))));
    }

    public static ExecutorService getSyncDataToHiveExecutor() {
        return DATA_THREAD_POOL_EXECUTOR.get();
    }
}
