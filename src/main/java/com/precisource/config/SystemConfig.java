package com.precisource.config;

import com.bleach.Logs;
import com.bleach.common.StringUtils;
import com.bleach.file.SimpleProperties;
import org.apache.commons.compress.utils.Lists;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: xinput
 * @Date: 2020-06-10 21:48
 */
public class SystemConfig {

    private static final Logger logger = Logs.get();

    private static final String ADD_HEADERS = "addHeaders";

    private static SimpleProperties SP;

    static {
        String fileName = "system.properties";
        try {
            SP = SimpleProperties.readConfiguration(fileName);
        } catch (Exception e) {
            SP = null;
        }
    }

    public static final List<String> getHeader() {
        if (SP == null) {
            return Lists.newArrayList();
        }

        String[] headerArray = SP.getStringProperty(ADD_HEADERS).split(StringUtils.COMMA);
        List<String> headers = Lists.newArrayList();
        Arrays.stream(headerArray).forEach(header -> headers.add(header));

        return headers;
    }
}
