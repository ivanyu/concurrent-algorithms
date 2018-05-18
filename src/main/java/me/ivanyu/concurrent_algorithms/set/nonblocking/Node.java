package me.ivanyu.concurrent_algorithms.set.nonblocking;

import java.util.concurrent.atomic.AtomicMarkableReference;

class Node {
    final int key;
    final AtomicMarkableReference<Node> next = new AtomicMarkableReference<Node>( null, false);

    Node(final int key) {
        this.key = key;
    }
}
