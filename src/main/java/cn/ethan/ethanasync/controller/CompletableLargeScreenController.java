package cn.ethan.ethanasync.controller;

import cn.ethan.ethanasync.bean.LargeScreenReq;
import cn.ethan.ethanasync.bean.LargeScreenResp;
import cn.ethan.ethanasync.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 使用CompletableFuture优化大屏接口
 *
 * @author Ethan
 **/
@Slf4j
@RestController
public class CompletableLargeScreenController {

    @Resource
    private ThreadPoolExecutor customThreadPoolExecutor;

    @Resource
    private DemoService demoService;


    @GetMapping(value = "/getAllData")
    public LargeScreenResp getAllData(LargeScreenReq largeScreenReq) {
        // 获取护理等级数据
        CompletableFuture<Map<String, Object>> future1 = CompletableFuture.supplyAsync(() -> this.buildNursingLevel(largeScreenReq), customThreadPoolExecutor);
        // 获取在职员工数据
        CompletableFuture<Map<String, Object>> future2 = CompletableFuture.supplyAsync(() -> this.buildCurrentEmp(largeScreenReq), customThreadPoolExecutor);
        // 获取长者画像数据
        CompletableFuture<Map<String, Object>> future3 = CompletableFuture.supplyAsync(() -> this.buildPortraitElderly(largeScreenReq), customThreadPoolExecutor);
        // 获取健康分析数据
        CompletableFuture<Map<String, Object>> future4 = CompletableFuture.supplyAsync(() -> this.buildHealthxAnalysis(largeScreenReq), customThreadPoolExecutor);

        CompletableFuture<Void> allOfFuture = CompletableFuture.allOf(future1, future2, future3, future4);
        CompletableFuture<LargeScreenResp> resultFuture = allOfFuture.thenApply(v -> {
            LargeScreenResp largeScreenDTO = new LargeScreenResp();
            try {
                largeScreenDTO.setNursingLevel(future1.get());
                largeScreenDTO.setCurrentEmp(future2.get());
                largeScreenDTO.setPortraitElderly(future3.get());
                largeScreenDTO.setHealthxAnalysis(future4.get());
                return largeScreenDTO;
            } catch (Exception e) {
                log.error("[Error] assemble largeScreenDTO data error: {}", e);
                throw new RuntimeException(e);
            }
        });
        return resultFuture.join();
    }

    public Map<String, Object> buildNursingLevel(LargeScreenReq largeScreenReq) {
        demoService.getDataTest();
        sleep(500);
        return new HashMap<>();
    }

    public Map<String, Object> buildCurrentEmp(LargeScreenReq largeScreenReq) {
        demoService.getDataTest();
        sleep(500);
        return new HashMap<>();
    }


    public Map<String, Object> buildPortraitElderly(LargeScreenReq largeScreenReq) {
        demoService.getDataTest();
        sleep(500);
        return new HashMap<>();
    }

    public Map<String, Object> buildHealthxAnalysis(LargeScreenReq largeScreenReq) {
        demoService.getDataTest();
        sleep(500);
        return new HashMap<>();
    }



    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
