package com.study.xiaolinzi.service;


import com.study.xiaolinzi.service.impl.FeignHystrixClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ：xiaolinzi
 * @date ：2020-5-14 3:05
 */
@FeignClient(name = "servier-provider1", url = "127.0.0.1:10010",fallback = FeignHystrixClientFallBack.class)
public interface FeignHystrixClient {

    @RequestMapping("/testFeginHystrix.do")
    String testFeginHystrix();

}
