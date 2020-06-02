package com.xiaolinzi.study.redislock.config;


import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：xiaolinzi
 * @date ：2020-6-2 13:50
 * @email: xiaolinzi95_27@163.com
 */
@Configuration
public class RedissonConfig {
    /**
     * redis集群方式
     *
     * @return
     */
    @Bean
    public RedissonClient redisson() {
        Config config = new Config();
        //redis集群方式
        config.useClusterServers().setScanInterval(2000)
                .addNodeAddress("redis://127.0.0.1:7000").addNodeAddress("redis://127.0.0.1:7001").setPassword("abcdef");
        return Redisson.create(config);
    }

//    /**
//     * redis单机方式
//     * @return
//     */
//    @Bean
//    public RedissonClient redisson() {
//        Config config = new Config();
//        //redis单机方式
//        config.useSingleServer()
//                .setAddress("redis://127.0.0.1:7000").setPassword("abcdef");
//        return Redisson.create(config);
//    }

}
