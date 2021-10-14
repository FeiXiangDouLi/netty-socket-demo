/**
 * 
 */
package com.example.socket.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wuzheng@tiduyun.com
 *
 */
@Slf4j
public class JSONUtils {

    private static final ObjectMapper JACKSON_OBJECT_MAPPER = createJacksonObjectMapper();

    private static ObjectMapper createJacksonObjectMapper() {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//        return mapper;
        ObjectMapper mapper = new ObjectMapper();
        // 允许未知的属性
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 允许单引号字段名
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 允许空的bean
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 设置时间序列化
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE));
        // 全局日期反序列化配置
        SimpleModule module = new SimpleModule();
        module.addDeserializer(java.sql.Date.class, new DateDeserializers.SqlDateDeserializer());
        module.addDeserializer(java.sql.Timestamp.class, new DateDeserializers.TimestampDeserializer());
        mapper.registerModule(module);
        return mapper;
    }

    public static String toJSONString(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        return JACKSON_OBJECT_MAPPER.writeValueAsString(object);
    }

    public static String toJSONStringIgnoreErrors(Object object) {
        try {
            return toJSONString(object);
        } catch (Exception e) {
            log.warn("Serialize object to json failed: object={}", object, e);
            return null;
        }
    }

    public static <T> T parse(String json, Class<T> clazz) throws JsonProcessingException, IOException {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        return JACKSON_OBJECT_MAPPER.readValue(json, clazz);
    }

    public static <T> T parse(Object jsonObject, Class<T> clazz) throws JsonProcessingException, IOException {
        if (jsonObject == null) {
            return null;
        }
        String json;
        if (jsonObject instanceof String) {
            json = (String)jsonObject;
        } else {
            json = toJSONString(jsonObject);
        }
        return parse(json, clazz);
    }

    public static <T> T parseIgnoreErrors(String json, Class<T> clazz, T defaultValue) {
        try {
            T result = parse(json, clazz);
            if (result != null) {
                return result;
            }
        } catch (Exception e) {
            log.warn("Parse object from json failed: json={}, class={}", json, clazz, e);
        }
        return defaultValue;
    }

    public static <T> T parseIgnoreErrors(Object jsonObject, Class<T> clazz, T defaultValue) {
        try {
            T result = parse(jsonObject, clazz);
            if (result != null) {
                return result;
            }
        } catch (Exception e) {
            log.warn("Parse object from json failed: jsonObject={}, class={}", jsonObject, clazz, e);
        }
        return defaultValue;
    }

    public static <T> T parse(String json, JavaType javaType) throws JsonProcessingException, IOException {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        return JACKSON_OBJECT_MAPPER.readValue(json, javaType);
    }

    public static <T> T parse(Object jsonObject, JavaType javaType) throws JsonProcessingException, IOException {
        if (jsonObject == null) {
            return null;
        }
        String json;
        if (jsonObject instanceof String) {
            json = (String)jsonObject;
        } else {
            json = toJSONString(jsonObject);
        }
        return parse(json, javaType);
    }

    public static <T> T parseIgnoreErrors(String json, JavaType javaType, T defaultValue) {
        try {
            T result = parse(json, javaType);
            if (result != null) {
                return result;
            }
        } catch (Exception e) {
            log.warn("Parse object from json failed: json={}, javaType={}", json, javaType, e);
        }
        return defaultValue;
    }

    public static <T> T parseIgnoreErrors(Object jsonObject, JavaType javaType, T defaultValue) {
        try {
            T result = parse(jsonObject, javaType);
            if (result != null) {
                return result;
            }
        } catch (Exception e) {
            log.warn("Parse object from json failed: jsonObject={}, javaType={}", jsonObject, javaType, e);
        }
        return defaultValue;
    }

    public static <T> T parseIgnoreErrors(String json, Class<T> clazz) {
        return parseIgnoreErrors(json, clazz, null);
    }

    public static <T> T parseIgnoreErrors(Object jsonObject, Class<T> clazz) {
        return parseIgnoreErrors(jsonObject, clazz, null);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseMap(String json) throws JsonProcessingException, IOException {
        return parse(json, Map.class);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseMapIgnoreErrors(String json) {
        return parseIgnoreErrors(json, Map.class, null);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseMapIgnoreErrors(String json, Map<String, Object> defaultValue) {
        return parseIgnoreErrors(json, Map.class, defaultValue);
    }

    public static <T> List<T> parseList(String json, Class<T> clazz) throws JsonProcessingException, IOException {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        List<T> list = JACKSON_OBJECT_MAPPER.readValue(json,
            TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, clazz));
        return list;
    }

    public static <T> List<T> parseListIgnoreErrors(String json, Class<T> clazz) {
        return parseListIgnoreErrors(json, clazz, null);
    }

    public static <T> List<T> parseListIgnoreErrors(String json, Class<T> clazz, List<T> defaultValue) {
        try {
            List<T> result = parseList(json, clazz);
            if (result != null) {
                return result;
            }
        } catch (Exception e) {
            log.warn("Parse object from json failed: json={}, class={}", json, clazz, e);
        }
        return defaultValue;
    }

}
