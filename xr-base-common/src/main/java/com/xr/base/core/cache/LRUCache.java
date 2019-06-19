package com.xr.base.core.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Time: 2019-03-28 11:56
 * @Author: forvoyager@outlook.com
 * @Description: LRU缓存（最近最少使用），主要思想：
 * 固定缓存大小，需要给缓存分配一个固定的大小。
 * 每次读取缓存都会改变缓存的使用时间，将缓存的存在时间重新刷新。
 * 需要在缓存满了后，将最近最久未使用的缓存删除，再添加最新的缓存。
 * <p>
 * 利用LinkedHashMap来构造LRU缓存，原因：
 * accessOrder为true的时候，就按访问顺序对LinkedHashMap排序，为false的时候就按插入顺序，默认是为false的。 当把accessOrder设置为true后，就可以将最近访问的元素置于最前面。
 * 重写removeEldestEntry，返回true的时候，就会remove其中最久的元素。
 */
public class LRUCache<K, V> {

  private final int MAX_CACHE_SIZE;
  private final float DEFAULT_LOAD_FACTORY = 0.75f;

  // 读多写少
  private ReadWriteLock lock = new ReentrantReadWriteLock();

  private LinkedHashMap<K, V> cache;

  public LRUCache(int size) {
    MAX_CACHE_SIZE = size;

    /**
     * (缓存大小 / loadFactor)+1，这样就可以在元素数目达到缓存大小时，也不会进行扩容，因为：
     * 触发扩容前，removeEldestEntry会把最久的元素删除
     */
    int capacity = (int) Math.ceil(MAX_CACHE_SIZE / DEFAULT_LOAD_FACTORY) + 1;

    /**
     * 第三个参数设置为true，代表linkedlist按访问顺序排序，可作为LRU缓存
     * 第三个参数设置为false，代表按插入顺序排序，可作为FIFO缓存
     */
    cache = new LinkedHashMap<K, V>(capacity, DEFAULT_LOAD_FACTORY, true) {
      @Override
      protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        // 缓存的元素大于MAX_CACHE_SIZE时，返回true，删除其中最久的元素。
        return size() > MAX_CACHE_SIZE;
      }
    };
  }

  public void put(K key, V value) {
    lock.writeLock().lock();
    try {
      cache.put(key, value);
    } finally {
      lock.writeLock().unlock();
    }
  }

  public V get(K key) {
    lock.readLock().lock();
    try {
      return cache.get(key);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public String toString() {
    return cache.toString();
  }
}
