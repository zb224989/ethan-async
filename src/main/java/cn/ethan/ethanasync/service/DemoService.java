package cn.ethan.ethanasync.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author lihaoxiang@jbinfo.cn
 * @ClassName DemoService
 * @description: TODO
 * @datetime 2024年 04月 23日 09:50
 * @version: 1.0
 */
@Service
public class DemoService {
    @Async
    public String asyncTest() {
        System.out.println("已进入到异步 - 线程名称："+Thread.currentThread().getName() + " be ready to read data!");
        return "已进入到异步";
    }
}
