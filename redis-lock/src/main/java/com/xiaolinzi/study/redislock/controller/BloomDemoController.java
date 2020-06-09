package com.xiaolinzi.study.redislock.controller;


import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.nio.charset.Charset;

/**
 * @author ：xiaolinzi
 * @date ：2020-6-9 16:19
 * @email: xiaolinzi95_27@163.com
 */
@RestController
public class BloomDemoController {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final BloomFilter<CharSequence> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.forName("utf-8")), 1000, 0.001);

    /**
     * @return done
     */
    @ResponseBody
    @GetMapping(value = "/bloomSetKeyDemo", produces = "application/json")
    public String bloomSetKeyDemo() {

        String key = "bloomKey";
        String value = "bloomValue";

        boolean result = bloomFilter.put(key);

        if (!result) {
            return "fail";
        }
        stringRedisTemplate.opsForValue().set(key, value);
        return "done";
    }

    /**
     * @return value
     */
    @ResponseBody
    @GetMapping(value = "/bloomGetKeyDemo", produces = "application/json")
    public String bloomGetKeyDemo() {
        String key = "bloomKey";
        boolean exist = bloomFilter.mightContain(key);

        if (!exist) {
            return "not exist ";
        }
        return stringRedisTemplate.opsForValue().get(key);

    }
}
