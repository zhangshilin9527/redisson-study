package com.study.xiaolinzi.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.study.xiaolinzi.service.FeignHystrixClient;
import com.study.xiaolinzi.service.TestFeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ：xiaolinzi
 * @date ：2020-5-14 3:14
 */
@RestController
public class HystrixFeginController {
    @Resource
    private FeignHystrixClient feignHystrixClient;

    /**
     * fegin整合hystrix
     * 注意
     *  1.需要整合fegin和hystrix 打开配置
     *  2.实现fegin
     *  3.定义fallback类，并交给spring管理
     *
     * @return
     */
    @RequestMapping("/testFeginHystrix.do")
    public String testDemotion() {
        System.out.println("测试降级");


        return feignHystrixClient.testFeginHystrix();
    }


}
