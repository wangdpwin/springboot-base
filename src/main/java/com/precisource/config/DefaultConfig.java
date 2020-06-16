package com.precisource.config;

import com.bleach.common.StringUtils;
import com.bleach.file.SimpleProperties;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: xinput
 * @Date: 2020-06-10 21:48
 */
public class DefaultConfig {

    public static final String DEFAULT_MODE = "prod";

    private static SimpleProperties SP;

    static {
        try {
            SP = SimpleProperties.readConfiguration("system.properties");
        } catch (Exception e) {
            SP = null;
        }
    }

    public static final List<String> getHeader() {
        if (SP == null) {
            return Lists.newArrayList();
        }

        String[] headerArray = SP.getStringProperty("addHeaders").split(StringUtils.COMMA);
        List<String> headers = Lists.newArrayList();
        Arrays.stream(headerArray).forEach(header -> headers.add(header));

        return headers;
    }

    /**
     * get SECRET_KEY
     *
     * @return
     */
    public static final String getSecretKey() {
        return get("application.secret", "xinput");
    }

    /**
     * 获取开发模式
     *
     * @return
     */
    public static final String getMode() {
        return get("application.mode", DEFAULT_MODE);
    }

    public static final String getMockUserId() {
        return get("mock.userId");
    }

    public static final String getMockUserName() {
        return get("mock.name");
    }

    /**
     * cookie中token的值
     */
    public static final String getCookieTokenName() {
        return get("api.token.cookie", "jwt");
    }

    /**
     * cookie验证
     */
    public static final boolean getCookieSecure() {
        return getBoolean("application.session.secure", Boolean.FALSE);
    }

    /**
     * 获取自定义key对应的value
     *
     * @param key
     * @return
     */
    public static final String get(String key) {
        return get(key, StringUtils.EMPTY);
    }

    /**
     * 获取自定义key对应的value,如果不存在，使用默认值 defaultValue
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static final String get(String key, String defaultValue) {
        if (SP == null) {
            return StringUtils.EMPTY;
        }

        return SP.getStringProperty(key, defaultValue);
    }

    public static final boolean getBoolean(String key) {
        return getBoolean(key, Boolean.FALSE);
    }

    public static final boolean getBoolean(String key, boolean defaultValue) {
        if (SP == null) {
            return Boolean.FALSE;
        }

        return SP.getBooleanProperty(key, defaultValue);
    }

    public static final List<String> getList(String key) {
        return getList(key, Lists.newArrayList());
    }

    public static final List<String> getList(String key, List<String> defaultList) {
        if (SP == null) {
            return defaultList;
        }

        String[] arrs = SP.getStringArrayProperty(key);
        if (ArrayUtils.isEmpty(arrs)) {
            return Lists.newArrayList(arrs);
        }

        return defaultList;
    }
}
