package cn.ethan.ethanasync.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * CompletableFuture Demo
 * @author Ethan
 **/
@Slf4j
@RestController
public class CompletableFutureDemoController {

    @Resource
    private ThreadPoolExecutor customThreadPoolExecutor;


    /**
     *  ====================  runAsync()、thenRun()、thenRunAsync() ====================
     */
    @GetMapping(value = "/thenRun")
    public String thenRun() {
//        runAsync()、thenRun()
        CompletableFuture.runAsync(() -> {
            sleep(1000);
            log.info(getThreadName() + " first step...");
        }).thenRun(() -> {
            sleep(1000);
            log.info(getThreadName() + " second step...");
        }).thenRunAsync(() -> {
            sleep(1000);
            log.info(getThreadName() + " third step...");
        });
        return "success";
    }


    @GetMapping(value = "/thenApply")
    public String thenApply() {
        CompletableFuture.supplyAsync(() -> {
            log.info(getThreadName() + " first step...");
            return "hi,";
        }).thenApply((result1) -> {
            String targetResult = result1 + "ethan";
            log.info("first step result: " + result1);
            log.info(getThreadName() + " second step..., thenApply Result: " + targetResult);
            return targetResult;
        });
        return "success";
    }


    @GetMapping(value = "/thenAccept")
    public String thenAccept() {
        CompletableFuture.supplyAsync(() -> {
            log.info(getThreadName() + " first step...");
            return "hello";
        }).thenAccept((result1) -> {
            String targetResult = result1 + " ethan";
            log.info("first step result: " + result1);
            log.info(getThreadName() + " second step..., targetResult: " + targetResult);
        });
        return "success";
    }


    /**
     *  ====================  thenCombine()、thenCompose() ====================
     */
    @GetMapping(value = "/thenCombine")
    public void thenCombine() {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            log.info("执行future1开始...");
            return "Hello";
        });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            log.info("执行future2开始...");
            return "Ethan";
        });
        future1.thenCombine(future2, (result1, result2) -> {
            String result = result1 + " " + result2;
            log.info("获取到future1、future2聚合结果：" + result);
            return result;
        }).thenAccept(result -> log.info(result));
    }



    @GetMapping(value = "/thenCompose")
    public void thenCompose() {
        CompletableFuture.supplyAsync(() -> {
            // 第一个Future实例结果
            log.info(getThreadName() + " 执行future1开始...");
            return "Hello";
        }).thenCompose(result1 -> CompletableFuture.supplyAsync(() -> {
            // 将上一个Future实例结果传到这里
            log.info(getThreadName() + " 执行future2开始..., 第一个实例结果：" + result1);
            return result1 + " Ethan";
        })).thenCompose(result12 -> CompletableFuture.supplyAsync(() -> {
            // 将第一个和第二个实例结果传到这里
            log.info(getThreadName() + " 执行future3开始..., 第一第二个实现聚合结果：" + result12);
            String targetResult = result12 + ",  I hope you're having a great day";
            log.info("最终输出结果：" + targetResult);
            return targetResult;
        }));
    }






    /**
     * 获取线程名称
     */
    private static String getThreadName() {
        return "【" + Thread.currentThread().getName() + "】";
    }

    /**
     * 休眠方法
     */
    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
