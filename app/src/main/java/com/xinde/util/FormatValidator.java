package com.xinde.util;

import java.util.regex.Pattern;

/**
 * 用与数据有效性检查的工具
 */
public class FormatValidator {
    // 电话号码REGEX
    public static final String REGEX_MOBILE = "^((\\+86)|(86))?((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";

    // 身份证REGEX
    public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";

    // URL REGEX (只是演示性)
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    public static boolean isIDCard(String idCard) {
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }

    public static boolean isUrl(String url) {
        return Pattern.matches(REGEX_URL, url);
    }
}
