package com.study.xiaolinzi.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.study.xiaolinzi.service.TestFeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ：xiaolinzi
 * @date ：2020-5-14 1:44
 */
@RestController
public class HystrixTestController {
    @Resource
    private TestFeignClient feignClient;

    /**
     * 降级方法
     * 注意：
     * 1.使用@EnableHystrix标签开启hystrix
     * 2.fallBack方法要和原方法的入参类型，出参一样,否则汇报 Incompatible return types. 异常
     *
     * @return
     */
    @RequestMapping("/testDemotion.do")
    @HystrixCommand(fallbackMethod = "testDemotionFallbackMethod")
    public String testDemotion() {
        System.out.println("测试降级");

        feignClient.testDemotion();
        return "ok";
    }

    public String testDemotionFallbackMethod() {
        System.out.println("进入降级的Fallback方法");

        return "已经降级";
    }

    /**
     * 超时监听方法
     * 注意：
     * 1.hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds 配置命令执行超时时 间，默认1000ms，标识1s内不返回结果则进入降级方法
     *
     * @return
     */
    @RequestMapping("/testTimeOut.do")
    @HystrixCommand(fallbackMethod = "testTimeOutFallbackMethod")
    public String testTimeOut() {
        System.out.println("测试超时降级");

        feignClient.testTimeout();
        return "ok";
    }

    public String testTimeOutFallbackMethod() {
        System.out.println("进入降级的Fallback方法");

        return "已经超时降级";
    }

    /**
     * 熔断
     * 注意：
     * 1.hystrix.command.default.circuitBreaker.requestVolumeThreshold 一段时间内请求失败的阈值。如果设为20，那么当在一段时间内如果调用失败超过20次，则熔断。默认20
     * 2.hystrix.command.default.circuitBreaker.sleepWindowInMilliseconds 触发熔断的时间阈值 默认5000
     * 当1.和2共同使用默认值的时候，表示5秒内超过20次请求失败，则熔断
     * 3.hystrix.command.default.circuitBreaker.enabled   半打开熔断，进行健康检查
     *
     * @return
     */
    @RequestMapping("/testFusing.do")
    @HystrixCommand(fallbackMethod = "testFusingFallbackMethod")
    public String testFusing() {
        System.out.println("测试熔断");

        feignClient.testFusing();
        return "ok";
    }

    public String testFusingFallbackMethod() {
        System.out.println("进入熔断的Fallback方法");

        return "已经熔断";
    }

    /**
     * 熔断
     * 注意：
     * 1.hystrix.threadpool.default.coreSizehystrix.threadpool.default.coreSize 默认线程池，当超过处理请求超过此阈值时候，进行限流
     * 2.hystrix.threadpool.default.maxQueueSize  等待队列，当上面的线程满了，放入此队列，若超过 队列+线程请求书，则限流
     *
     * @return
     */
    @RequestMapping("/testLimiting.do")
    @HystrixCommand(fallbackMethod = "testLimitingFallbackMethod", threadPoolKey = "testLimit", threadPoolProperties = {@HystrixProperty(name = "coreSize", value = "5")})
    public String testLimiting() {
        System.out.println("测试限流");

        feignClient.testLimiting();
        return "ok";
    }

    public String testLimitingFallbackMethod() {
        System.out.println("进入限流的Fallback方法");

        return "已经限流";
    }


}
