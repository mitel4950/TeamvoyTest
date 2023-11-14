package com.example.teamvoytest.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;

class ProductLockServiceTest {

  private final ProductLockService productLockService = new ProductLockService();

  @Test
  void multipleThreadsShouldNotInterfere() throws InterruptedException {
    CountDownLatch startSignal = new CountDownLatch(3);
    CountDownLatch endSignal = new CountDownLatch(3);
    AtomicBoolean thread1Running = new AtomicBoolean(false);
    AtomicBoolean thread2Running = new AtomicBoolean(false);

    Set<Long> productIdsThread1 = Set.of(1L, 2L);
    Set<Long> productIdsThread2 = Set.of(3L, 4L);

    Thread thread1 = new Thread(target(productIdsThread1, thread1Running, startSignal, endSignal));
    Thread thread2 = new Thread(target(productIdsThread2, thread2Running, startSignal, endSignal));

    thread1.start();
    thread2.start();

    startSignal.countDown();
    startSignal.await();
    assertTrue(thread1Running.get() && thread2Running.get(),
               "Both threads must run simultaneously");
    endSignal.countDown();
    thread1.join();
    thread2.join();
  }

  private Runnable target(Set<Long> productIdsThread,
                          AtomicBoolean threadRunning,
                          CountDownLatch startSignal,
                          CountDownLatch endSignal) {
    return () -> {
      productLockService.lockProductIds(productIdsThread);
      try {
        threadRunning.set(true);
        startSignal.countDown();
        endSignal.countDown();
        endSignal.await();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      } finally {
        threadRunning.set(false);
        productLockService.unlockProductIds(productIdsThread);
      }
    };
  }

  @Test
  void secondThreadShouldWaitForFirstThread() throws InterruptedException {
    CountDownLatch startSignal = new CountDownLatch(2);
    CountDownLatch endSignal = new CountDownLatch(2);
    AtomicBoolean thread1Running = new AtomicBoolean(false);
    AtomicBoolean thread2Running = new AtomicBoolean(false);

    Thread thread1 = new Thread(target(Set.of(1L, 2L), thread1Running, startSignal, endSignal));
    Thread thread2 = new Thread(target(Set.of(2L, 3L), thread2Running, startSignal, endSignal));

    thread1.start();
    thread2.start();

    startSignal.countDown();
    startSignal.await();
    assertTrue(thread1Running.get() ^ thread2Running.get(),
               "Only one threads must run");
    endSignal.countDown();
    thread1.join();
    thread2.join();
  }
}

