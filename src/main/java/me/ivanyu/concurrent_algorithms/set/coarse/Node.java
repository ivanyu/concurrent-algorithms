package me.ivanyu.concurrent_algorithms.set.coarse;

class Node {
    final int key;
    volatile Node next;

    Node(final int key) {
        this.key = key;
    }
}
