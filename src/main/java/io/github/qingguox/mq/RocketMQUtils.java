package io.github.qingguox.mq;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

/**
 * @author wangqingwei
 * Created on 2022-07-11
 */
@Component
public class RocketMQUtils {

    private static final Logger logger = LoggerFactory.getLogger(RocketMQUtils.class);

    private static final List<Long> DEFAULT_LEVEL;

    static {
        DEFAULT_LEVEL = Lists.newArrayList();
        DEFAULT_LEVEL.add(TimeUnit.SECONDS.toMillis(1));
        DEFAULT_LEVEL.add(TimeUnit.SECONDS.toMillis(5));
        DEFAULT_LEVEL.add(TimeUnit.SECONDS.toMillis(10));
        DEFAULT_LEVEL.add(TimeUnit.SECONDS.toMillis(30));
        DEFAULT_LEVEL.add(TimeUnit.MINUTES.toMillis(1));
        DEFAULT_LEVEL.add(TimeUnit.MINUTES.toMillis(2));
        DEFAULT_LEVEL.add(TimeUnit.MINUTES.toMillis(3));
        DEFAULT_LEVEL.add(TimeUnit.MINUTES.toMillis(4));
        DEFAULT_LEVEL.add(TimeUnit.MINUTES.toMillis(5));
        DEFAULT_LEVEL.add(TimeUnit.MINUTES.toMillis(6));
        DEFAULT_LEVEL.add(TimeUnit.MINUTES.toMillis(7));
        DEFAULT_LEVEL.add(TimeUnit.MINUTES.toMillis(8));
        DEFAULT_LEVEL.add(TimeUnit.MINUTES.toMillis(9));
        DEFAULT_LEVEL.add(TimeUnit.MINUTES.toMillis(10));
        DEFAULT_LEVEL.add(TimeUnit.MINUTES.toMillis(20));
        DEFAULT_LEVEL.add(TimeUnit.MINUTES.toMillis(30));
        DEFAULT_LEVEL.add(TimeUnit.HOURS.toMillis(1));
        DEFAULT_LEVEL.add(TimeUnit.HOURS.toMillis(2));
    }


    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public RocketMQTemplate getRocketMQTemplate() {
        return rocketMQTemplate;
    }

    public void setRocketMQTemplate(RocketMQTemplate rocketMQTemplate) {
        this.rocketMQTemplate = rocketMQTemplate;
    }

    public SendResult sendSyncMsg(Message<? extends BaseMessage> msg, String topic, long timeOutMills) {
        //1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h rocketMQ自动支持18个级别 等级全部转化为秒
        //            message.setDelayTimeLevel(2);                           // 5s
        return rocketMQTemplate.syncSend(topic, msg, timeOutMills); // 3秒超时
    }

    public SendResult sendDelaySyncMsg(Message<? extends BaseMessage> msg, String topic, long timeOutMills, long delayMills) {
        BaseMessage payload = msg.getPayload();
        long targetTimeStamp = payload.getTimeStamp();
        long curTimeStamp = System.currentTimeMillis();
        if (targetTimeStamp <= curTimeStamp) {
            // 立即发送
        }
        return rocketMQTemplate.syncSend(topic, msg, timeOutMills, 0);
    }
}
