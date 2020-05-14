package com.study.xiaolinzi.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ：xiaolinzi
 * @date ：2020-5-14 0:05
 */
@FeignClient(name = "servier-provider", url = "127.0.0.1:10010")
public interface TestFeignClient {

    @RequestMapping("/testFegin.do")
    String testFegin();

    /**
     * 降级
     *
     * @return
     */
    @RequestMapping("/testDemotion.do")
    String testDemotion();

    /**
     * 超时降级
     *
     * @return
     */
    @RequestMapping("/testTimeout.do")
    String testTimeout();

    /**
     * 熔断
     *
     * @return
     */
    @RequestMapping("/testFusing.do")
    String testFusing();
    /**
     * 限流
     *
     * @return
     */
    @RequestMapping("/testLimiting.do")
    String testLimiting();

}
