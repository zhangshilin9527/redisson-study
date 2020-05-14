package com.study.xiaolinzi.service.impl;

import com.study.xiaolinzi.service.FeignHystrixClient;
import org.springframework.stereotype.Component;

/**
 * @author ：xiaolinzi
 * @date ：2020-5-14 3:15
 */
@Component
public class FeignHystrixClientFallBack implements FeignHystrixClient {
    @Override
    public String testFeginHystrix() {
        return "FeignHystrixClientFallBack进行降级";
    }
}
