package com.study.xiaolinzi.controller;

import com.study.xiaolinzi.service.TestFeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ：xiaolinzi
 * @date ：2020-5-14 0:02
 */
@RestController
public class FeginTestController {

    @Resource
    private TestFeignClient feignClient;

    @RequestMapping("/testClientFegin.do")
    public String testClientFegin() {
        String response = feignClient.testFegin();
        System.out.println(response);
        return "ok";
    }
}
