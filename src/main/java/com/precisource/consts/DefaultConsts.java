package com.precisource.consts;

/**
 * System.properties中的属性
 *
 * @Author: xinput
 * @Date: 2020-06-10 21:48
 */
public class DefaultConsts {

    public enum MODE {
        DEV("dev"),
        TEST("test"),
        PROD("prod");

        private String profile;

        MODE(String profile) {
            this.profile = profile;
        }

        public String getProfile() {
            return profile;
        }
    }

    /**
     * 默认配置文件名称
     */
    public static final String DEFAULT_SYSTEM_FILE = "system.properties";

    /**
     * 该系统默认返回的access-control-expose-headers中目前仅包含以下这些
     * Origin, Authorization, Content-Type, If-Match, If-Modified-Since, If-None-Match,
     * If-Unmodified-Since, Accept-Encoding, X-Request-Id, X-Total-Count
     * <p>
     * 在system.properties中可以通过设置header.add参数来额外添加其他需要的值
     */
    public static final String HEADER_ADD = "header.add";

    /**
     * 在开发环境下默认使用的用户Id
     */
    public static final String MOCK_USER_ID = "mock.userId";

    /**
     * 在开发环境下默认使用的用户名称
     */
    public static final String MOCK_USER_NAME = "mock.userName";

    /**
     * 设置Cookie中token对应的key值，默认是jwt
     */
    public static final String API_COOKIE_TOKEN = "api.cookie.token";

    /**
     * 是否开启Cookie验证，默认关闭
     */
    public static final String API_COOKIE_ENABLE = "api.cookie.enable";

    /**
     * 是否默认开启全局的token验证
     * 在开发环境下，默认关闭
     * 在测试环境和正式环境下，默认开启，如果某个方法不想验证，可以使用 @PassSecure 注解
     */
    public static final String API_SECURE_ENABLE = "api.secure";

    /**
     * token过期时间
     */
    public static final String API_TOKEN_EXPIRE = "token.exp";

    /**
     * Token刷新时间
     */
    public static final String API__REFRESH_TOKEN_EXPIRE = "refresh.token.exp";

    /**
     * secure的key
     */
    public static final String API_SECRET_KEY = "api.secret.key";

    /**
     * 微信小程序设置:wechat.appid
     */
    public static final String WECHAT_APPID = "wechat.appid";

    /**
     * 微信小程序设置:wechat.secret
     */
    public static final String WECHAT_SECRET = "wechat.secret";

    /**
     * 对象存储的key
     */
    public static final String BUCKET_ACCESS_KEY = "bucket.access.key";

    /**
     * 对象存储的秘钥
     */
    public static final String BUCKET_SECRET_KEY = "bucket.access.secret";
}
