package io.github.qingguox.id.sequence.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ClientIdCache {
    private long leftId;
    private long curId;
    private long rightId;

    public ClientIdCache() {
    }

    public ClientIdCache(long leftId, long curId, long rightId) {
        this.leftId = leftId;
        this.curId = curId;
        this.rightId = rightId;
    }

    public long getLeftId() {
        return leftId;
    }

    public void setLeftId(long leftId) {
        this.leftId = leftId;
    }

    public long getRightId() {
        return rightId;
    }

    public void setRightId(long rightId) {
        this.rightId = rightId;
    }

    public long getCurId() {
        return curId;
    }

    public void setCurId(long curId) {
        this.curId = curId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}