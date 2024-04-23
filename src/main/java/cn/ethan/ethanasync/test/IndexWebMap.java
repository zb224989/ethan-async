package cn.ethan.ethanasync.test2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class IndexWebMap {
    public static void main(String[] args) {
        List<Supplier<ITask>> allTask = getSuppliers();

        CompletableFuture[] cfs = new CompletableFuture[allTask.size()];
        for (int i = 0; i < allTask.size(); i++) {
            cfs[i] = CompletableFuture.supplyAsync(allTask.get(i));
        }

        CompletableFuture<Void> allOfFuture = CompletableFuture.allOf(cfs);
        CompletableFuture<Map<String, Object>> objectCompletableFuture = allOfFuture.thenApply(v -> {
            Map<String, Object> taskResults = new HashMap<>();
            for (CompletableFuture<ITask> cf : cfs) {
                try {
                    ITask iTask = cf.get();
                    taskResults.put(iTask.getKey(), iTask.getValue());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
            return taskResults;
        });
        System.out.println("等待结果");
        Map<String, Object> join = objectCompletableFuture.join();
        System.out.println(join);
    }

    private static List<Supplier<ITask>> getSuppliers() {
        List<Supplier<ITask>> allTask = new ArrayList<>();
        allTask.add(IndexWebMap::buildBanners);
        allTask.add(IndexWebMap::buildBanners2);
        return allTask;
    }

    private static ITask buildBanners() {
        Task stringTask = new Task();
        stringTask.setKey("bbb");
        stringTask.setValue("vvv");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return stringTask;
    }
    private static ITask buildBanners2() {
        Task stringTask = new Task();
        stringTask.setKey("aaa");
        stringTask.setValue("ddd");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return stringTask;
    }

    private static ITask buildBanners3() {
        Task stringTask = new Task();
        stringTask.setKey("aaa");
        stringTask.setValue("ddd");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return stringTask;
    }
}