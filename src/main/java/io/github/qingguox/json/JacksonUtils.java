package io.github.qingguox.json;

import static com.fasterxml.jackson.core.JsonFactory.Feature.INTERN_FIELD_NAMES;
import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS;
import static com.fasterxml.jackson.core.json.JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.hubspot.jackson.datatype.protobuf.ProtobufModule;

/**
 * @author wangqingwei
 * Created on 2022-08-06
 */
public class JacksonUtils {

    private static final String DEFAULT_EMPTY_JSON = "{}";

    private static final ObjectMapper MAPPER = new ObjectMapper(new JsonFactoryBuilder()
            .configure(INTERN_FIELD_NAMES, !INTERN_FIELD_NAMES.enabledByDefault())
            .build());

    static {
        MAPPER.registerModule(new GuavaModule());
        MAPPER.disable(FAIL_ON_UNKNOWN_PROPERTIES);
        MAPPER.enable(ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature());
        MAPPER.enable(ALLOW_COMMENTS);
        MAPPER.registerModule(new ParameterNamesModule());
        MAPPER.registerModule(new KotlinModule());
        MAPPER.registerModule(new ProtobufModule());
    }

    public static String toJSON(@Nullable Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new UncheckedJsonProcessingException(e);
        }
    }

    public static void toJSON(@Nullable Object obj, OutputStream os) {
        if (obj == null) {
            return;
        }
        try {
            MAPPER.writeValue(os, obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T fromJSON(@Nullable InputStream is, Class<T> value) {
        try {
            return MAPPER.readValue(is, TypeFactory.defaultInstance().constructType(value));
        } catch (IOException e) {
            throw wrapException(e);
        }
    }

    public static <T> T fromJSON(@Nullable byte[] bytes, Class<T> value) {
        if (bytes == null) {
            return null;
        }
        try {
            return MAPPER.readValue(bytes, value);
        } catch (IOException e) {
            throw wrapException(e);
        }
    }

    public static <T> T fromJSON(@Nullable String json, Class<T> value) {
        if (json == null) {
            return null;
        }
        try {
            return MAPPER.readValue(json, value);
        } catch (IOException e) {
            throw wrapException(e);
        }
    }

    public static <T> T fromJSON(@Nullable String json, TypeReference<T> type) {
        if (json == null) {
            return null;
        }
        try {
            return MAPPER.readValue(json, type);
        } catch (IOException e) {
            throw wrapException(e);
        }
    }

    public static <T> T fromJSON(@Nullable String json, JavaType valueType) {
        if (json == null) {
            return null;
        }
        try {
            return MAPPER.readValue(json, valueType);
        } catch (IOException e) {
            throw wrapException(e);
        }
    }

    public static <T> T fromJSON(Object obj, Class<T> valueType) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return fromJSON((String) obj, valueType);
        }
        if (obj instanceof byte[]) {
            return fromJSON((byte[]) obj, valueType);
        }
        return null;
    }

    public static <E, T extends Collection<E>> T fromJSON(InputStream is, Class<? extends Collection> collectionType,
            Class<E> valueType) {
        try {
            return MAPPER
                    .readValue(is, TypeFactory.defaultInstance().constructCollectionType(collectionType, valueType));
        } catch (IOException e) {
            throw wrapException(e);
        }
    }

    public static <E, T extends Collection<E>> T fromJSON(String json, Class<? extends Collection> collectionType,
            Class<E> valueType) {
        try {
            return MAPPER
                    .readValue(json, TypeFactory.defaultInstance().constructCollectionType(collectionType, valueType));
        } catch (JsonProcessingException e) {
            throw new UncheckedJsonProcessingException(e);
        }
    }

    public static <E, T extends Collection<E>> T fromJSON(byte[] bytes, Class<? extends Collection> collectionType,
            Class<E> valueType) {
        try {
            return MAPPER
                    .readValue(bytes, TypeFactory.defaultInstance().constructCollectionType(collectionType, valueType));
        } catch (IOException e) {
            throw wrapException(e);
        }
    }

    public static <K, V, T extends Map<K, V>> T fromJSON(InputStream is, Class<? extends Map> mapType, Class<K> keyType,
            Class<V> valueType) {
        try {
            return MAPPER.readValue(is, TypeFactory.defaultInstance().constructMapType(mapType, keyType, valueType));
        } catch (IOException e) {
            throw wrapException(e);
        }
    }

    public static <K, V, T extends Map<K, V>> T fromJSON(String json, Class<? extends Map> mapType, Class<K> keyType,
            Class<V> valueType) {
        if (StringUtils.isEmpty(json)) {
            json = DEFAULT_EMPTY_JSON;
        }

        try {
            return MAPPER.readValue(json, TypeFactory.defaultInstance().constructMapType(mapType, keyType, valueType));
        } catch (JsonProcessingException e) {
            throw new UncheckedJsonProcessingException(e);
        }
    }

    public static <K, V, T extends Map<K, V>> T fromJSON(byte[] bytes, Class<? extends Map> mapType, Class<K> keyType,
            Class<V> valueType) {
        try {
            return MAPPER.readValue(bytes, TypeFactory.defaultInstance().constructMapType(mapType, keyType, valueType));
        } catch (IOException e) {
            throw wrapException(e);
        }
    }

    public static Map<String, Object> fromJson(InputStream is) {
        return fromJSON(is, Map.class, String.class, Object.class);
    }

    public static Map<String, Object> fromJson(String json) {
        return fromJSON(json, Map.class, String.class, Object.class);
    }

    public static Map<String, Object> fromJson(byte[] bytes) {
        return fromJSON(bytes, Map.class, String.class, Object.class);
    }

    /**
     * 转换obj为指定类型.
     * 前提是: 需要判断obj是否是type
     * @param obj 对象
     * @param type 类型
     * @param <T>
     * @return 类型
     */
    public static <T> T value(Object obj, Class<T> type) {
        return MAPPER.convertValue(obj, type);
    }

    /**
     * @param obj 对象
     * @param type 类型
     * @param <T>
     * @return 类型
     */
    public static <T> T value(Object obj, JavaType type) {
        return MAPPER.convertValue(obj, type);
    }

    /**
     * @param obj 对象
     * @param type 类型
     * @param <T>
     * @return 类型
     */
    public static <T> T value(Object obj, TypeReference<T> type) {
        return MAPPER.convertValue(obj, type);
    }

    private static RuntimeException wrapException(IOException exp) {
        if (exp instanceof JsonProcessingException) {
            return new UncheckedJsonProcessingException(exp);
        }
        return new UncheckedIOException(exp);
    }
}

