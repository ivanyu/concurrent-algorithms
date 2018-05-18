package me.ivanyu.concurrent_algorithms.set.lazy;

import me.ivanyu.concurrent_algorithms.set.Set;

public class LazySyncSet implements Set<Integer> {
    private final Node head;

    public LazySyncSet() {
        this.head = new Node(Integer.MIN_VALUE);
        this.head.next = new Node(Integer.MAX_VALUE);
    }

    @Override
    public boolean add(final Integer x) {
        while (true) {
            Node pred = head;
            Node curr = head.next;
            while (curr.key < x) {
                pred = curr;
                curr = curr.next;
            }

            pred.lock();
            try {
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
                    curr.unlock();
                }
            } finally {
                pred.unlock();
            }
        }
    }

    @Override
    public boolean remove(final Integer x) {
        while (true) {
            Node pred = head;
            Node curr = head.next;

            while (curr.key < x) {
                pred = curr;
                curr = curr.next;
            }

            pred.lock();
            try {
                curr.lock();
                try {
                    if (!validate(pred, curr)) {
                        continue;
                    }

                    if (curr.key == x) {
                        curr.isDeleted = true;
                        pred.next = curr.next;
                        return true;
                    } else {
                        return false;
                    }
                } finally {
                    curr.unlock();
                }
            } finally {
                pred.unlock();
            }
        }
    }

    @Override
    public boolean contains(final Integer x) {
        Node curr = head;
        while (curr.key < x) {
            curr = curr.next;
        }
        return curr.key == x && !curr.isDeleted;
    }

    private boolean validate(final Node pred, final Node curr) {
        return !pred.isDeleted && !curr.isDeleted && pred.next == curr;
    }
}
