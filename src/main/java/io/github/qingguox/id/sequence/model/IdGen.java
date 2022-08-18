package io.github.qingguox.id.sequence.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author wangqingwei
 * Created on 2022-08-16
 */
public class IdGen {
    private long id;
    private long maxId;
    private int step;
    private long version;
    private long bizId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMaxId() {
        return maxId;
    }

    public void setMaxId(long maxId) {
        this.maxId = maxId;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public long getBizId() {
        return bizId;
    }

    public void setBizId(long bizId) {
        this.bizId = bizId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
