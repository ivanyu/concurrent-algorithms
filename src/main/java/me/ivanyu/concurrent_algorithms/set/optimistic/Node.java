package me.ivanyu.concurrent_algorithms.set.optimistic;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Node {
    private final Lock lock = new ReentrantLock();

    final int key;
    volatile Node next;

    Node(final int key) {
        this.key = key;
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }
}
