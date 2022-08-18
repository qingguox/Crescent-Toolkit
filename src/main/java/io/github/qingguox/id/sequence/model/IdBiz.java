package io.github.qingguox.id.sequence.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author wangqingwei
 * Created on 2022-08-16
 */
public class IdBiz {
    private long id;
    private String bizType;
    private int rule;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public int getRule() {
        return rule;
    }

    public void setRule(int rule) {
        this.rule = rule;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
