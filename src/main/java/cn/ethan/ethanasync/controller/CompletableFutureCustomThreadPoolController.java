package cn.ethan.ethanasync.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * CompletableFuture使用自定义线程池
 *
 * @author Ethan
 **/
@Slf4j
@RestController
public class CompletableFutureCustomThreadPoolController {

    @Resource
    private ThreadPoolExecutor customThreadPoolExecutor;

    /**
     * CompletableFuture使用自定义线程池
     *
     * @throws ExecutionException /
     * @throws InterruptedException /
     */
    @GetMapping(value = "/allOfCustomThreadPool")
    public void allOf() throws ExecutionException, InterruptedException {
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println(getThreadName() + " cf1 do something....");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("cf1 任务完成");
            return "cf1 任务完成";
        }, customThreadPoolExecutor);

        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println(getThreadName() + " cf2 do something....");
//                int a = 1/0;
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("cf2 任务完成");
            return "cf2 任务完成";
        }, customThreadPoolExecutor);

        CompletableFuture<String> cf3 = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println(getThreadName() + " cf2 do something....");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("cf3 任务完成");
            return "cf3 任务完成";
        }, customThreadPoolExecutor);

        CompletableFuture<Void> cfAll = CompletableFuture.allOf(cf1, cf2, cf3);
        System.out.println("cfAll结果->" + cfAll.get());
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
