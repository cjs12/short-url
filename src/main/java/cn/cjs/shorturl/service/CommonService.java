package cn.cjs.shorturl.service;

import cn.cjs.shorturl.ConstantUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CommonService {

    /**
     * 十进制数字转为62进制数字
     * @param digit
     * @return
     */
    public String incrementShortUrl(long digit) {
        StringBuilder sb = new StringBuilder();
        int key = ConstantUtil.SEED_ARRAY.length;
        do {
            sb.append(ConstantUtil.SEED_ARRAY[(int) digit % key]);
        } while ((digit = digit / key) > key);
        // 第一位不需要加0
        if (digit != 0) {
            sb.append(ConstantUtil.SEED_ARRAY[(int) digit]);
        }
        sb.reverse();

        Random random = new Random();
        int randomArrayLength = ConstantUtil.RANDOM_ARRAY.length;
        while (sb.length() < ConstantUtil.SHORT_URL_MAX_LENGTH) {
            sb.append(ConstantUtil.RANDOM_ARRAY[random.nextInt(randomArrayLength)]);
        }

        return sb.toString();
    }

    /**
     * 算法2，根据MD5计算短链接
     * @param originUrl
     * @return
     */
    public List<String> md5ShortUrl(String originUrl) {
        String salt = "md5-salt";
        originUrl = salt + originUrl;
        // 1. 将长网址根据md5生成32位签名串，分为4段，每段8个字节
        String md5String = DigestUtils.md5DigestAsHex(originUrl.getBytes(StandardCharsets.UTF_8));

        List<String> stringList = new ArrayList<>();
        Matcher matcher = ConstantUtil.EIGHT_CHAR_PATTERN.matcher(md5String);
        while (matcher.find()) {
            stringList.add(matcher.group());
        }

        // 2. 对列表循环
        // 取 8 个字节, 将他看成 16 进制串与 0x3fffffff(30位1) 与操作, 即超过 30 位的忽略处理
        long seedLength = ConstantUtil.SEED_ARRAY.length;
        Random random = new Random();
        stringList = stringList.stream()
                .map(str -> {
                    // 只截取30位长度
                    long temp = Long.parseLong(str, 16) & 0x3fffffff;
                    // 最终生成长度为6的字符串，所以每次向右偏移5位
                    // 然后为了尽可能多的使用字数组中字符，使用数组长度 - 1 作为最大值
                    StringBuilder result = new StringBuilder();
                    for (int i = 0; i < ConstantUtil.SHORT_URL_MAX_LENGTH; i++) {
                        int index = (int) (temp & 63);
                        if (index > seedLength - 1) {
                            index = random.nextInt((int) seedLength);
                        }
                        result.append(ConstantUtil.SEED_ARRAY[index]);
                        // 30 是因为 0x3fffffff转为二进制是30个1
                        temp = temp >> (30 / ConstantUtil.SHORT_URL_MAX_LENGTH);
                    }
                    return result.toString();
                })
                .collect(Collectors.toList());

        return stringList;
    }
}
