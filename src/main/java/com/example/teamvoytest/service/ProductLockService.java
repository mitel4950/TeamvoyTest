package com.example.teamvoytest.service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.stereotype.Service;

@Service
public class ProductLockService {

  private final ConcurrentHashMap<Long, Lock> lockMap = new ConcurrentHashMap<>();

  public void lockProductIds(Set<Long> productIds) {
    for (Long id : productIds) {
      lockMap.computeIfAbsent(id, k -> new ReentrantLock()).lock();
    }
  }

  public void unlockProductIds(Set<Long> productIds) {
    for (Long id : productIds) {
      Lock lock = lockMap.get(id);
      if (lock != null) {
        lock.unlock();
      }
    }
  }

}
