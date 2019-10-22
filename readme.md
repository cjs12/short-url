## First： 配置/src/main/resources/application.yml下redis配置

## 参考： [短网址(short URL)系统的原理及其实现](https://hufangyun.com/2017/short-url/)

## 摘要

### 什么是短链接

>就是把普通网址，转换成比较短的网址。比如：http://t.cn/RlB2PdD 这种，在微博这些限制字数的应用里。好处不言而喻。短、字符少、美观、便于发布、传播.

* 百度短网址 http://dwz.cn/

### 原理解析，以访问 http://t.cn/RlB2PdD 为例
1. DNS首先解析获得 http://t.cn 的 IP 地址
2. 当 DNS 获得 IP 地址以后（比如：74.125.225.72），会向这个地址发送 HTTP GET 请求，查询短码 RlB2PdD
3. http://t.cn 服务器会通过短码 RlB2PdD 获取对应的长 URL
4. 请求通过 HTTP 301 转到对应的长 URL https://m.helijia.com 。

### 算法实现

### 算法1： 自增序列算法（永不重复算法）

设置 id 自增，一个 10进制 id 对应一个 62进制的数值，1对1，也就不会出现重复的情况。这个利用的就是低进制转化为高进制时，字符数会减少的特性。

短址的长度一般设为 6 位，而每一位是由 [a - z, A - Z, 0 - 9] 总共 62 个字母组成的，所以 6 位的话，总共会有 62^6 ~= 568亿种组合，基本上够用了。


### 算法2：摘要算法

1. 将长网址 md5 生成 32 位签名串,分为 4 段, 每段 8 个字节
2. 对这四段循环处理, 取 8 个字节, 将他看成 16 进制串与 0x3fffffff(30位1) 与操作, 即超过 30 位的忽略处理
3. 这 30 位分成 6 段, 每 5 位的数字作为字母表的索引取得特定字符, 依次进行获得 6 位字符串
4. 总的 md5 串可以获得 4 个 6 位串,取里面的任意一个就可作为这个长 url 的短 url 地址

`虽然生成四个短链接，仍存在重复几率`

---
## 实现思路

1. 本地nginx配置域名解析
2. 代码分别实现两种算法，用mysql或redis实现 `短链接 <==> 长链接`的相互查询和转换
3. 使用`ResponseEntity`调整返回响应码为`301`


## 短链生成

1. spring-boot 2.2.0
2. redis 
3. jdk 1.8

## 算法一  redis设计

| key | 类型 | 备注 |
|---|---|---|
| short_url_increment | string | 自增值用于生成短链地址 |
| short_url_convert | map | 记录短链对应长链、长链对应短链的关系 |

### 参考
* [短网址(short URL)系统的原理及其实现](https://hufangyun.com/2017/short-url/)
* [短码生成算法](gitlab.xxt.cn/cjs/backend-study/wikis/短码生成算法)
* [springboot2.x整合redis实现缓存（附github链接）](https://www.cnblogs.com/coder-lichao/p/10889457.html)
