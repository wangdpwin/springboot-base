package com.precisource.util;

import com.precisource.annotation.Remark;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

    @Remark("逗号")
    public static final String COMMA = ",";

    @Remark("冒号")
    public static final String COLON = ":";

    @Remark("分号")
    public static final String SEMICOLON = ";";

    @Remark("斜杠")
    public static final String SLASH = "/";

    @Remark("手机号长度")
    public static final int PHONE_length = 11;

    @Remark("15位身份证号码长度")
    public static final int IDCARD_15_LENGTH = 15;

    @Remark("18位身份证号码长度")
    public static final int IDCARD_18_LENGTH = 18;

    public static final String EXAMPLE_PASSWORD = "******";
}
