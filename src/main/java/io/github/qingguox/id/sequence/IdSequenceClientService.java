package io.github.qingguox.id.sequence;

/**
 * @author wangqingwei
 * Created on 2022-08-18
 */
public interface IdSequenceClientService {

    long getId(long bizId);

    IdRule supportRule();
}
