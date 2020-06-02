package com.xiaolinzi.study.redislock;


import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RedisLockApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisLockApplication.class, args);
	}


}
