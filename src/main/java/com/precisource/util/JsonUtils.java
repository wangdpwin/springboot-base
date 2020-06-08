package com.precisource.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: xinput
 * json格式化
 */
public class JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private static ObjectMapper mapper = new ObjectMapper();

    public static String toJsonString(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将对象字符串(不是List格式),转化成对象.
     *
     * @param content
     * @param clazz
     * @return
     */

    public static <T> T toBean(String content, Class<T> clazz) {
        T t = null;
        try {
            t = mapper.readValue(content, clazz);
        } catch (JsonParseException e) {
            logger.error(e.getMessage(), e);
        } catch (JsonMappingException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return t;
    }

    /**
     * 将对象reader(不是List格式),转化成对象.
     *
     * @param reader
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T toBean(Reader reader, Class<T> clazz) {
        T t = null;
        try {
            t = mapper.readValue(reader, clazz);
        } catch (JsonParseException e) {
            logger.error(e.getMessage(), e);
        } catch (JsonMappingException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return t;
    }

    /**
     * 将对象InputStream(不是List格式),转化成对象.
     *
     * @param stream
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T toBean(InputStream stream, Class<T> clazz) {
        T t = null;
        try {
            t = mapper.readValue(stream, clazz);
        } catch (JsonParseException e) {
            logger.error(e.getMessage(), e);
        } catch (JsonMappingException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return t;
    }

    /**
     * 将List对象字符串,转化成List对象.
     *
     * @param content 字符串内容
     * @param clazz   对象类型 例如 User.class
     * @return
     */
    public static <T> List<T> toList(String content, Class<T> clazz) {
        return (List<T>) readValueList(content, ArrayList.class, clazz);
    }

    /**
     * 将List对象字符串,转化成List对象.
     *
     * @param content         字符串内容
     * @param collectionClass 集合类型,例如 ArrayList.class
     * @param clazz           对象类型 例如 User.class
     * @return
     */
    private static Object readValueList(String content, Class collectionClass, Class clazz) {
        Object o = null;

        try {
            o = mapper.readValue(content, getCollectionType(collectionClass, clazz));
        } catch (JsonParseException e) {
            logger.error(e.getMessage(), e);
        } catch (JsonMappingException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return o;
    }

    private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }
}
