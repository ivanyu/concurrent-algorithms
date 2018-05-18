package me.ivanyu.concurrent_algorithms.set.fine;

import me.ivanyu.concurrent_algorithms.set.Set;

public class FineGrainedSyncSet implements Set<Integer> {
    private final Node head;

    public FineGrainedSyncSet() {
        this.head = new Node(Integer.MIN_VALUE);
        this.head.next = new Node(Integer.MAX_VALUE);
    }

    @Override
    public boolean add(final Integer x) {
        head.lock();
        Node pred = head;
        try {
            Node curr = pred.next;
            curr.lock();
            try {
                while (curr.key < x) {
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                }

                if (x == curr.key) {
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

    @Override
    public boolean remove(final Integer x) {
        Node pred = null;
        head.lock();
        try {
            pred = head;
            Node curr = pred.next;
            curr.lock();
            try {
                while (curr.key < x) {
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                }

                if (x == curr.key) {
                    pred.next = curr.next;
                    return true;
                }

                return false;
            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    @Override
    public boolean contains(final Integer x) {
        Node pred = null;
        head.lock();
        try {
            pred = head;
            Node curr = pred.next;
            curr.lock();
            try {
                while (curr.key < x) {
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                }
                return x == curr.key;
            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }
}
