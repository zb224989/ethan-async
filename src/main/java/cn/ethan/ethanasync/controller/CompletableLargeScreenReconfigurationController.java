package cn.ethan.ethanasync.controller;

import cn.ethan.ethanasync.bean.LargeScreenResp;
import cn.ethan.ethanasync.bean.base.ILargeScreenParam;
import cn.ethan.ethanasync.bean.LargeScreenReq;
import cn.ethan.ethanasync.bean.base.LargeScreenKeyEnum;
import cn.ethan.ethanasync.bean.base.LargeScreenParam;
import cn.ethan.ethanasync.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Supplier;

/**
 * 使用CompletableFuture优化大屏接口
 * 重构版
 *
 * @author Ethan
 **/
@Slf4j
@RestController
public class CompletableLargeScreenReconfigurationController {

    @Resource
    private ThreadPoolExecutor customThreadPoolExecutor;

    @Resource
    private DemoService demoService;

    @Resource
    private ApplicationContext applicationContext;


    @GetMapping(value = "/getAllDataReconfiguration")
    public Object getAllData(LargeScreenReq largeScreenReq) {

        // 反射获取所有任务
        List<Supplier<ILargeScreenParam>> allTask = getAllTask(largeScreenReq);
        if (CollectionUtils.isEmpty(allTask)) {
            return "{}";
        }

        // 提交所有任务
        List<CompletableFuture<ILargeScreenParam>> cfs = new ArrayList<>(allTask.size());
        allTask.forEach(task -> cfs.add(CompletableFuture.supplyAsync(task, customThreadPoolExecutor)));
        CompletableFuture<Void> allOfFuture = CompletableFuture.allOf(cfs.toArray(new CompletableFuture[allTask.size()]));

        // 获取所有任务的结果
        CompletableFuture<Map<String, Object>> objectCompletableFuture = allOfFuture.thenApply(v -> {
            Map<String, Object> taskResults = new HashMap<>(allTask.size());
            cfs.forEach(future -> {
                ILargeScreenParam iTask;
                try {
                    iTask = future.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
                taskResults.put(iTask.getKey(), iTask.getValue());
            });
            return taskResults;
        });

        // 返回
        System.out.println("等待结果");
        Map<String, Object> join = objectCompletableFuture.join();
        System.out.println(join);
        return join;
    }

    public List<Supplier<ILargeScreenParam>> getAllTask(LargeScreenReq LargeScreenReq) {

        CompletableLargeScreenReconfigurationController bean = applicationContext.getBean(this.getClass());

        //获取公共方法
        Method[] methods = bean.getClass().getMethods();
        ArrayList<Method> allMethods = new ArrayList<>();
        for (Method method : methods) {
            if (method.getName().startsWith("build")) {
                allMethods.add(method);
            }
        }

        List<Supplier<ILargeScreenParam>> allTask = new ArrayList<>();
        for (Method m : allMethods) {
            allTask.add(() -> {
                try {
                    Class<?>[] parameterTypes = m.getParameterTypes();
                    if (parameterTypes.length == 0) {
                        return (ILargeScreenParam) m.invoke(bean);
                    }
                    return (ILargeScreenParam) m.invoke(bean, LargeScreenReq);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return allTask;
    }

    public ILargeScreenParam buildNursingLevel(LargeScreenReq largeScreenReq) {
        demoService.getDataTest();
        ArrayList<Object> list = new ArrayList<>();
        list.add(new LargeScreenParam("介助1", 10));
        list.add(new LargeScreenParam("介助2", 30));
        list.add(new LargeScreenParam("介助3", 40));
        LargeScreenParam stringTask = new LargeScreenParam();
        stringTask.setKey(LargeScreenKeyEnum.NURSING_LEVEL.getCode());
        stringTask.setValue(list);

        sleep(500);
        return stringTask;
    }
    public ILargeScreenParam buildCurrentEmp(LargeScreenReq largeScreenReq) {
        LargeScreenParam stringTask = new LargeScreenParam();
        stringTask.setKey(LargeScreenKeyEnum.CURRENT_EMP.getCode());
        stringTask.setValue("73");
        sleep(500);
        return stringTask;
    }
    public ILargeScreenParam buildPortraitElderly(LargeScreenReq largeScreenReq) {
        LargeScreenParam stringTask = new LargeScreenParam();
        stringTask.setKey(LargeScreenKeyEnum.PORTRAIT_ELDERLY.getCode());
        stringTask.setValue("73");
        sleep(500);
        return stringTask;
    }
    public ILargeScreenParam buildHealthxAnalysis(LargeScreenReq largeScreenReq) {
        LargeScreenParam stringTask = new LargeScreenParam();
        stringTask.setKey(LargeScreenKeyEnum.HEALTHX_ANALYSIS.getCode());
        stringTask.setValue("73");
        sleep(500);
        return stringTask;
    }
    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
