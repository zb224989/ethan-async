package cn.ethan.ethanasync.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public interface DemoService {

    /**
     * @Async 异步方法
     **/
    String asyncTest();


    /**
     * 普通业务方法
     **/
    String getDataTest();

}
