package cn.cjs.shorturl;

import java.util.regex.Pattern;

/**
 * 常量类
 * @author Administrator
 */
public interface ConstantUtil {
    /** 前缀 */
    String SHORT_URL_PREFIX = "http://t.cn/";

    /** 自增序列 */
    String SHORT_URL_INCREMENT = "short_url_increment";

    /** Map结构，长链对应短链，短链对应长链 */
    String SHORT_URL_CONVERT = "short_url_convert";

    /** 短码最大长度 */
    int SHORT_URL_MAX_LENGTH = 6;

    /**56进制,未使用的字母用于长度补充*/
    String[] SEED_ARRAY = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z", "A", "B", "C", "D",
            "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T"
    };

    /** 短链接长度太短时，从下面选取字符随机补充 */
    String[] RANDOM_ARRAY = {"U", "V", "W", "X", "Y",  "Z"};

    /** 截取八位字符串正则 */
    Pattern EIGHT_CHAR_PATTERN = Pattern.compile(".{8}");

    /** 是否有协议头正则 */
    Pattern HTTP_PATTERN = Pattern.compile("https?://");
}
