package cn.cjs.shorturl.controller;

import cn.cjs.shorturl.ConstantUtil;
import cn.cjs.shorturl.service.CommonService;
import cn.cjs.shorturl.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
public class ShortUrlController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private CommonService commonService;

    /**
     * 根据短链接返回长链接，且响应码为301
     * @return
     */
    @GetMapping("/{shortUrl}")
    public ResponseEntity<String> getOriginUrl(@PathVariable("shortUrl") String shortUrl) {
        String originUrl = redisService.get(ConstantUtil.SHORT_URL_CONVERT, shortUrl);
        if (StringUtils.isEmpty(originUrl)) {
            originUrl = "http://www.baidu.com";
//            throw new RuntimeException("404 Not Found");
        }

        // 需要添加http协议头，否则会成为工程内部路径
        if (!ConstantUtil.HTTP_PATTERN.matcher(originUrl.toLowerCase()).lookingAt()) {
            originUrl = "http://" + originUrl;
        }

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("location", originUrl);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    @PostMapping("/convert-short-url")
    public String getShortUrl(@Valid @RequestBody String url) {
        // 算法一，自增
        String convertKey = ConstantUtil.SHORT_URL_CONVERT;
        // 判断长链是否有对应内容
        String shortUrl = redisService.get(convertKey, url);
        if (StringUtils.isEmpty(shortUrl)) {
            // 获取当前序列值
            long sequence = redisService.increment(ConstantUtil.SHORT_URL_INCREMENT);
            shortUrl = commonService.incrementShortUrl(sequence);

            // 在redis中记录短链 -- 长链的映射关系
            redisService.put(convertKey, shortUrl, url);
            redisService.put(convertKey, url, shortUrl);
        }
        return ConstantUtil.SHORT_URL_PREFIX + shortUrl;
    }
}
