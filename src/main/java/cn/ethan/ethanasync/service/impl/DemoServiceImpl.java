package cn.ethan.ethanasync.service.impl;

import cn.ethan.ethanasync.service.DemoService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl implements DemoService {
    @Async
    public String asyncTest() {
        System.out.println("已进入到异步 - 线程名称："+Thread.currentThread().getName() + " be ready to read data!");
        return "已进入到异步";
    }

    public String getDataTest(){
        System.out.println("DemoServiceDemoService getDataTest");
        return "";
    }
}
