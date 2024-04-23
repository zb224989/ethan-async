package cn.ethan.ethanasync.controller;


import cn.ethan.ethanasync.bean.AbstractTask;
import cn.ethan.ethanasync.bean.ITask;
import cn.ethan.ethanasync.bean.Task;
import cn.ethan.ethanasync.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Supplier;

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

    @Resource
    ApplicationContext applicationContext;


    @GetMapping(value = "/getAllData")
    public Object getAllData() {
        List<Supplier<ITask>> allTask = getAllTask();
        if (CollectionUtils.isEmpty(allTask)) {
            return "null";
        }

        List<CompletableFuture<ITask>> cfs = new ArrayList<>(allTask.size());
        allTask.forEach(task -> cfs.add(CompletableFuture.supplyAsync(task)));
        CompletableFuture<Void> allOfFuture = CompletableFuture.allOf(cfs.toArray(new CompletableFuture[allTask.size()]));
        CompletableFuture<Map<String, Object>> objectCompletableFuture = allOfFuture.thenApply(v -> {
            Map<String, Object> taskResults = new HashMap<>(allTask.size());
            cfs.forEach(future -> {
                ITask iTask;
                try {
                    iTask = future.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
                taskResults.put(iTask.getKey(), iTask.getValue());
            });
            return taskResults;
        });
        System.out.println("等待结果");
        Map<String, Object> join = objectCompletableFuture.join();
        System.out.println(join);
        return join;
    }

    public List<Supplier<ITask>> getAllTask() {

        CompletableLargeScreenController bean = applicationContext.getBean(this.getClass());

        Class<? extends CompletableLargeScreenController> studentClass = bean.getClass();

        //获取公共方法
        Method[] methods = studentClass.getMethods();
        ArrayList<Method> allMethods = new ArrayList<>();
        for (Method method : methods) {
            if (method.getName().startsWith("build")) {
                allMethods.add(method);
            }
        }

        List<Supplier<ITask>> allTask = new ArrayList<>();
        for (Method m : allMethods) {
            allTask.add(() -> {
                try {
                    return (ITask) m.invoke(bean);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return allTask;
    }

    public ITask buildBanners() {
        Task stringTask = new Task();
        stringTask.setKey("bbb");
        stringTask.setValue("vvv");
        System.out.println("buildBannersbuildBannersbuildBannersbuildBanners");
        demoService.getDataTest();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return stringTask;
    }

    public ITask buildBanners2() {
        Task stringTask = new Task();
        stringTask.setKey("olderNumber");
        stringTask.setValue("73");
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return stringTask;
    }

}
