package com.study.xiaolinzi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：xiaolinzi
 * @date ：2020-5-14 1:44
 */
@RestController
public class HystrixTestController {


    /**
     * 测试fegin调用
     *
     * @return
     */
    @RequestMapping("/testFegin.do")
    public String testFegin() {

        return "ok";
    }

    /**
     * 抛异常，测试降级
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/testDemotion.do")
    public String testDemotion() throws Exception {
        throw new Exception("测试降级");
    }


    /**
     * 测试超时
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/testTimeout.do")
    public String testTimeout() throws Exception {
        Thread.sleep(3000);
        return "测试超时";
    }

    /**
     * 抛异常，测试熔断
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/testFusing.do")
    public String testFusing() throws Exception {
        throw new Exception("测试降级");
    }

    /**
     * 睡眠，测限流
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/testLimiting.do")
    public String testLimiting() throws Exception {
        Thread.sleep(3000);
        return "ok";
    }
    /**
     * 抛异常，测试降级
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/testFeginHystrix.do")
    public String testFeginHystrix() throws Exception {
        throw new Exception("测试降级");
    }


}
