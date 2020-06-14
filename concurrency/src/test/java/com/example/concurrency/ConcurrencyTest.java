package com.example.concurrency;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ConcurrencyTest {

    private static int clientTotal = 1000;
    private static int threadTotal = 50;
    private static AtomicInteger count = new AtomicInteger(0);
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for (int i= 0;i < clientTotal; i++) {
            executorService.execute(()->{
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    log.error("exception",e);
                }
                add();
                semaphore.release();
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        log.info("count:{}",count);
    }

    private static void add() {
        count.incrementAndGet();
    }
}
