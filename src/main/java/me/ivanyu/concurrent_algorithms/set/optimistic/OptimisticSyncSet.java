package me.ivanyu.concurrent_algorithms.set.optimistic;

import me.ivanyu.concurrent_algorithms.set.Set;

public class OptimisticSyncSet implements Set<Integer> {
    private final Node head;

    public OptimisticSyncSet() {
        this.head = new Node(Integer.MIN_VALUE);
        this.head.next = new Node(Integer.MAX_VALUE);
    }

    @Override
    public boolean add(final Integer x) {
        while (true) {
            Node pred = head;
            Node curr = pred.next;
            while (curr.key < x) {
                pred = curr;
                curr = curr.next;
            }

            pred.lock();
            curr.lock();
            try {
                if (!validate(pred, curr)) {
                    continue;
                }

                if (curr.key == x) {
                    return false;
                }

                final Node newNode = new Node(x);
                newNode.next = curr;
                pred.next = newNode;
                return true;
            } finally {
                pred.unlock();
                curr.unlock();
            }
        }
    }

    @Override
    public boolean remove(final Integer x) {
        while (true) {
            Node pred = head;
            Node curr = pred.next;
            while (curr.key < x) {
                pred = curr;
                curr = curr.next;
            }

            pred.lock();
            curr.lock();
            try {
                if (!validate(pred, curr)) {
                    continue;
                }

                if (curr.key == x) {
                    pred.next = curr.next;
                    return true;
                } else {
                    return false;
                }
            } finally {
                pred.unlock();
                curr.unlock();
            }
        }
    }

    @Override
    public boolean contains(final Integer x) {
        while (true) {
            Node pred = head;
            Node curr = pred.next;
            while (curr.key < x) {
                pred = curr;
                curr = curr.next;
            }

            pred.lock();
            curr.lock();
            try {
                if (validate(pred, curr)) {
                    return curr.key == x;
                }
            } finally {
                pred.unlock();
                curr.unlock();
            }
        }
    }

    private boolean validate(final Node pred, final Node curr) {
        Node node = head;
        while (node.key <= pred.key) {
            if (node == pred) {
                return pred.next == curr;
            }
            node = node.next;
        }
        return false;
    }
}
