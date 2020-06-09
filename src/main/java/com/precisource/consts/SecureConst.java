package com.precisource.consts;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

/**
 *
 * [ 通用常量 ]
 *
 * @version 1.0
 * @since JDK1.8
 * @author yandanyang
 * @company 1024lab.net
 * @copyright (c) 2019 1024lab.netInc. All rights reserved.
 * @date
 */
public class SecureConst {

    @Value("ignore.url")
    private static String ignoreUrl;

    @Value("ignore.url.mapping")
    private static String ignoreUrlMapping;

    public static final Set<String> IGNORE_URL = Sets.newHashSet();

    public static final Set<String> IGNORE_URL_MAPPING = Sets.newHashSet();

    static {
        String[] urls = ignoreUrl.split(",");
        String[] mappings = ignoreUrlMapping.split(",");
        Stream.of(urls).forEach((u) -> IGNORE_URL.add(u));
        Stream.of(mappings).forEach((m) -> IGNORE_URL_MAPPING.add(m));
    }

    public static Boolean containUrl(String uri) {
        if (CollectionUtils.isEmpty(IGNORE_URL)) {
            return false;
        }
        for (String ignoreUrl : IGNORE_URL) {
            if (uri.startsWith(ignoreUrl)) {
                return true;
            }
        }
        return false;
    }

    public static Boolean containMapping(String uri) {
        if (CollectionUtils.isEmpty(IGNORE_URL_MAPPING)) {
            return false;
        }
        for (String ignoreUrl : IGNORE_URL_MAPPING) {
            if (uri.startsWith(ignoreUrl)) {
                return true;
            }
        }
        return false;
    }

}