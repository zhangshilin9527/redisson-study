package com.xiaolinzi.study.redislock.controller;


import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author ：xiaolinzi
 * @date ：2020-5-28 16:19
 * @email: xiaolinzi95_27@163.com
 */
@RestController
public class RedisLockController {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    /**
     * redisLockDemo1 介绍：
     * 此处使用了stringRedisTemplate的setIfAbsent api {@link ValueOperations#setIfAbsent(java.lang.Object, java.lang.Object)}，setIfAbsent这个api对应的就是redis中的setnx命令
     * 使用setIfAbsent此api的时候需要注意，必须要释放锁，若不释放，则当前线程一直占据锁，导致其他线程无法获取到锁；
     * <p>
     * redisLockDemo1 缺陷：
     * 此处有一个比较严重的缺陷，没有设置锁的超时时间：我们在代码的第36行获取锁，在代码46行释放了锁，这样看起来没什么问题，
     * 但是程序获取到锁，代码执行到40行之后如果出现一些异常情况（机器宕机，运维部署系统等），则在redis中的锁就无法释放，其他线程就无法
     * 再次获取锁。
     * <p>
     * 若解决此问题，请参照{@link #redisLockDemo2()}
     *
     * @return done
     */
    @ResponseBody
    @GetMapping(value = "/redisLockDemo1", produces = "application/json")
    public String redisLockDemo1() {
        String redisKey = "redis_key_001";
        try {
            //1.获取锁
            Boolean getLock = stringRedisTemplate.opsForValue().setIfAbsent(redisKey, "极简版redis锁");
            if (!getLock) {
                return "未获取到redis锁";
            }
            //2.获取到redis锁，进行业务逻辑
            System.out.println("获取到redis锁");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //3.最后进行删除锁
            stringRedisTemplate.delete(redisKey);
        }
        return "done";
    }

    /**
     * redisLockDemo2此方式使用的是stringRedisTemplate的setIfAbsent  api{@link ValueOperations#setIfAbsent(java.lang.Object, java.lang.Object, long, java.util.concurrent.TimeUnit)}
     * 此api添加了锁的超时时间，解决了因为一些异常情况而引起的锁不能释放的问题。
     * <p>
     * redisLockDemo2 缺陷：
     * redisLockDemo2虽然解决了锁不能释放的问题，但是又带来了一个新问题，那就是锁被别的线程释放，这个即使起来有点绕。所以锁
     * 是如何被其他线程释放的在我的博客中@see <a href="https://blog.csdn.net/qq_38630810/article/details/106500034">redis分布式锁在项目中的实现一 手动实现redis分布式锁</a>给出了解释。
     * <p>
     * 若解决此问题，请参照{@link #redisLockDemo3()}
     *
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/redisLockDemo2", produces = "application/json")
    public String redisLockDemo2() {
        String redisKey = "redis_key_002";
        try {
            Boolean getLock = stringRedisTemplate.opsForValue().setIfAbsent(redisKey, "redis锁带有失效时间", 10, TimeUnit.SECONDS);
            if (!getLock) {
                return "未获取到redis锁";
            }
            //获取到redis锁，进行业务逻辑
            System.out.println("获取到redis锁");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stringRedisTemplate.delete(redisKey);
        }
        return "done";
    }

    /**
     * redisLockDemo3 ：使用uuid获取到随机字符串作为value，在删除key的时候进行获取redis中的value，如果一样则删除key，
     * 这样做看似解决了redisLockDemo2中的缺点，但还存在其他缺陷；
     * <p>
     * redisLockDemo3 缺陷：
     * 1.获取锁后，机器宕机或其他异常，后面的锁不会被释放，会锁10s中
     * 2.判断value相当不是原子操作，此处出现异常，也会出现bug
     * 3.代码没执行完，锁失效，其他线程获取到锁，这个bug就比较严重了，可能会引起好几个线程同时执行一段代码。
     * <p>
     * 若解决此问题，请参照{@link #redisLockDemo4()}，我们要重点介绍的框架 redisson <a href="https://redisson.org/">redisson</a>
     *
     * @return done
     */
    @ResponseBody
    @GetMapping(value = "/redisLockDemo3", produces = "application/json")
    public String redisLockDemo3() {
        String redisKey = "redis_key_003";
        String value = UUID.randomUUID().toString();
        try {
            Boolean getLock = stringRedisTemplate.opsForValue().setIfAbsent(redisKey, value, 10, TimeUnit.SECONDS);
            if (!getLock) {
                return "未获取到redis锁";
            }
            //获取到redis锁，进行业务逻辑
            System.out.println("获取到redis锁");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (value.equals(stringRedisTemplate.opsForValue().get(redisKey))) {
                stringRedisTemplate.delete(redisKey);
            }
        }
        return "done";
    }

    /**
     * redisLockDemo4:
     * 介绍了redis客户端redisson，redisson对redis使用大量lua脚本对redis进行了操作进行封装，redisson具体使用
     * 文档请参照<a href="https://github.com/redisson/redisson/wiki/Table-of-Content/">Table of Content</a>；redisson具
     * 体是怎么实现分布式锁以及如何保证锁不会失效的请参照博客<a href="https://blog.csdn.net/qq_38630810/article/details/106500951">redis分布式锁在项目中的实现二 Redisson的使用，原理及源码解读</a>
     *
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/redisLockDemo4", produces = "application/json")
    public String redisLockDemo4() {
        String redisKey = "redis_key_004";
        RLock redissonLock = redissonClient.getLock(redisKey);
        try {
            redissonLock.lock();
            //获取到redis锁，进行业务逻辑
            System.out.println("获取到redis锁");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            redissonLock.unlock();
        }
        return "done";
    }
}
