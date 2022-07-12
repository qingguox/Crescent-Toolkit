package io.github.qingguox.mq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wangqingwei
 * Created on 2022-07-11
 */
@SpringBootApplication(scanBasePackages = "io.github.*")
public class RocketMqUtilsApplicationTest {
    public static void main(String[] args) {
        SpringApplication.run(RocketMqUtilsApplicationTest.class, args);
    }
}
