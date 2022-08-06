package io.github.qingguox.json;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * 主要是抛出异常, 不让上游写try-catch
 * @author wangqingwei
 * Created on 2022-08-06
 */
public class UncheckedJsonProcessingException extends UncheckedIOException {
    public UncheckedJsonProcessingException(IOException cause) {
        super(cause.getMessage(), cause);
    }
}
