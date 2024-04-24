package cn.ethan.ethanasync.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * CompletableFuture API
 *
 * @author Ethan
 **/
@Slf4j
@RestController
public class CompletableFutureApiDemoController {

    /**
     * == thenRun()
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

    /**
     * == thenApply()
     */
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

    /**
     * == thenAccept()
     */
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
     * == whenComplete()
     */
    @GetMapping(value = "/whenComplete")
    public void whenComplete() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(getThreadName() + " task1 do something....");
            int a = 1 / 0;
            return 1;
        });

        CompletableFuture<Integer> cf2 = cf1.whenComplete((result, e) -> {
            System.out.println("上个任务结果：" + result);
            System.out.println("上个任务抛出异常：" + e);
            System.out.println(getThreadName() + " task2 do something....");
        });

//        //等待任务1执行完成
//        System.out.println("task1结果->" + cf1.get());
//        //等待任务2执行完成
        System.out.println("task2结果->" + cf2.get());
    }


    /**
     * == handle()、handleAsync()
     */
    @GetMapping(value = "/handle")
    public void handle() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(getThreadName() + " task1 do something....");
//            int a = 1/0;
            return 1;
        });

        CompletableFuture<Integer> cf2 = cf1.handle((result, e) -> {
            System.out.println("上个任务结果：" + result);
            System.out.println("上个任务抛出异常：" + e);
            System.out.println(getThreadName() + " task2 do something....");
            return result + 100;
        });
//        //等待任务2执行完成
        System.out.println("task2结果->" + cf2.get());
    }

    /**
     * == thenCombine()
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

    /**
     * == thenAcceptBoth()
     */
    @GetMapping(value = "/thenAcceptBoth")
    public void thenAcceptBoth() {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            log.info("执行future1开始...");
            return "Hello";
        });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            log.info("执行future2开始...");
            return " Ethan";
        });

        CompletableFuture<Void> cf3 = future1.thenAcceptBoth(future2, (a, b) -> {
            System.out.println(getThreadName() + " cf3 do something....");
            System.out.println(a + b);
        });
    }

    /**
     * == runAfterBoth()
     */
    @GetMapping(value = "/runAfterBoth")
    public void runAfterBoth() {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            log.info("执行future1开始...");
            return "Hello";
        });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            log.info("执行future2开始...");
            return "Ethan";
        });

        CompletableFuture<Void> cf3 = future1.runAfterBoth(future2, () -> {
            System.out.println(getThreadName() + " cf3 do something....");
        });
    }

    /**
     * == thenCompose()
     */
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
     * == allOf()
     */
    @GetMapping(value = "/allOf")
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
        });

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
        });

        CompletableFuture<String> cf3 = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println(getThreadName() + " cf2 do something....");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("cf3 任务完成");
            return "cf3 任务完成";
        });

        CompletableFuture<Void> cfAll = CompletableFuture.allOf(cf1, cf2, cf3);
        System.out.println("cfAll结果->" + cfAll.get());
    }

    /**
     * == anyOf()
     */
    @GetMapping(value = "/anyOf")
    public void anyOf() throws ExecutionException, InterruptedException {
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println(getThreadName() + " cf1 do something....");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("cf1 任务完成");
            return "cf1 任务完成";
        });

        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println(getThreadName() + " cf2 do something....");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("cf2 任务完成");
            return "cf2 任务完成";
        });

        CompletableFuture<String> cf3 = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println(getThreadName() + " cf2 do something....");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("cf3 任务完成");
            return "cf3 任务完成";
        });

        CompletableFuture<Object> cfAll = CompletableFuture.anyOf(cf1, cf2, cf3);
        System.out.println("cfAll结果->" + cfAll.get());
    }

    /**
     * == exceptionally()
     */
    @GetMapping(value = "/exceptionally")
    public void exceptionally() throws ExecutionException, InterruptedException {
        CompletableFuture<String> resultFuture = CompletableFuture.supplyAsync(() -> {
            log.info(getThreadName() + " start....");
            int i = 1 / 0;
            log.info("任务完成");
            return "任务完成";
        });
        // 业务方法，内部会发起异步rpc调用
        resultFuture.exceptionally(error -> {
            //通过exceptionally捕获异常，打印日志并返回默认值
            log.error("resultFuture Exception...", error);
            return null;
        });
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
