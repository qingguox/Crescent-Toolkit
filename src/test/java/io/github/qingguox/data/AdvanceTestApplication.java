package io.github.qingguox.data;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import io.github.qingguox.json.JacksonUtils;

/**
 * @author wangqingwei
 * Created on 2022-08-06
 */
@SpringBootApplication(scanBasePackages = "io.github.*")
public class AdvanceTestApplication implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(AdvanceTestApplication.class);

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private UserDao userDao;
    @Autowired
    private GroupDao groupDao;

    @BeforeEach
    void init() {
        userDao.setJdbcTemplate(namedParameterJdbcTemplate);
        groupDao.setJdbcTemplate(namedParameterJdbcTemplate);
    }


    @Override
    public void run(String... args) throws Exception {
//        testUser();
        testGroup();
    }

    private void testGroup() {
        List<User> users = testGetUser();
        final Group group = new Group(1, "小组1", users);

        logger.info("groups : {}", JacksonUtils.toJSON(group));
        final int count = groupDao.insert(group);
        logger.info("count : {}", count);

        final List<Group> groups = groupDao.geByLimit(10);
        logger.info("groups : {}", JacksonUtils.toJSON(groups));
    }

    private void testUser() {
        User user = new User(2, "张三");
        final int count = userDao.insert(user);
        logger.info("count : {}", count);

        testGetUser();
    }

    private List<User> testGetUser() {
        final List<User> users = userDao.geByLimit(10);
        logger.info("users : {}", JacksonUtils.toJSON(users));
        return users;
    }

    public static void main(String[] args) {
        SpringApplication.run(AdvanceTestApplication.class);
    }
}
