package io.github.qingguox.mq;

/**
 * 提供一个基础的消息接口, 其他消息需实现该接口
 * @author wangqingwei
 * Created on 2022-07-11
 */
public interface BaseMessage {

    /**
     * Message Send TimeStamp(ms)
     */
    default long getTimeStamp() {
        return System.currentTimeMillis();
    }
}

