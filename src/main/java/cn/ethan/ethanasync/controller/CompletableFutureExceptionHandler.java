package cn.ethan.ethanasync.controller;

import java.util.concurrent.CompletableFuture;

/**
 * 异常对比
 */
public class CompletableFutureExceptionHandler {
    public static CompletableFuture handle(int a, int b) {
        return CompletableFuture.supplyAsync(() -> a / b)
                .handle((result, ex) -> {
                    if (null != ex) {
                        System.out.println("handle error: " + ex.getMessage());
                        return 0;
                    } else {
                        return result;
                    }
                });
    }
    public static CompletableFuture whenComplete(int a, int b) {
        return CompletableFuture.supplyAsync(() -> a / b)
                .whenComplete((result, ex) -> {
                    if (null != ex) {
                        System.out.println("whenComplete error: " + ex.getMessage());
                    }
                });
    }
    public static void main(String[] args) {
        try {
            System.out.println("success: " + handle(10, 5).get());
            System.out.println("fail: " + handle(10, 0).get());
        } catch (Exception e) {
            System.out.println("catch exception= " + e.getMessage());
        }

        System.out.println("------------------------------------------------------------------");

        try {
            System.out.println("success: " + whenComplete(10, 5).get());
            System.out.println("fail: " + whenComplete(10, 0).get());
        } catch (Exception e) {
            System.out.println("catch exception=" + e.getMessage());
        }
    }
}