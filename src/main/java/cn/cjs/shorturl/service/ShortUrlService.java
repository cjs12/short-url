package cn.cjs.shorturl.service;

import cn.cjs.shorturl.ConstantUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShortUrlService {

    @Autowired
    private RedisService redisService;

    /**
     * 生成短链接
     * @param originUrl
     * @return
     */
    public String getShortUrl(String originUrl) {
        long seed = redisService.increment(ConstantUtil.SHORT_URL_INCREMENT);
        // seed转62进制

        return null;
    }

    /**
     * 获取原有链接
     * @param shortUrl
     * @return
     */
    public String getOriginUrl(String shortUrl) {
        return null;
    }
}
