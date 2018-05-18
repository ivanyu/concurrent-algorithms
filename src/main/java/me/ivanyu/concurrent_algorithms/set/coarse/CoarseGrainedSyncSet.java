package me.ivanyu.concurrent_algorithms.set.coarse;

import me.ivanyu.concurrent_algorithms.set.Set;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CoarseGrainedSyncSet implements Set<Integer> {
    private final Node head;

    private final Lock lock = new ReentrantLock();

    public CoarseGrainedSyncSet() {
        this.head = new Node(Integer.MIN_VALUE);
        this.head.next = new Node(Integer.MAX_VALUE);
    }

    @Override
    public boolean add(final Integer x) {
        Node pred;
        Node curr;
        lock.lock();
        try {
            pred = head;
            curr = pred.next;
            while (curr.key < x) {
                pred = curr;
                curr = curr.next;
            }

            if (x == curr.key) {
                return false;
            }

            final Node newNode = new Node(x);
            newNode.next = curr;
            pred.next = newNode;
            return true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean remove(final Integer x) {
        Node pred;
        Node curr;
        lock.lock();
        try {
            pred = head;
            curr = pred.next;
            while (curr.key < x) {
                pred = curr;
                curr = curr.next;
            }

            if (x == curr.key) {
                pred.next = curr.next;
                return true;
            } else {
                return false;
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean contains(final Integer x) {
        Node curr;
        lock.lock();
        try {
            curr = head.next;
            while (curr.key < x) {
                curr = curr.next;
            }
            return x == curr.key;
        } finally {
            lock.unlock();
        }
    }
}
