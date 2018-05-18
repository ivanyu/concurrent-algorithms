package me.ivanyu.concurrent_algorithms.set.nonblocking;

import me.ivanyu.concurrent_algorithms.set.Set;

public class NonBlockingSet implements Set<Integer> {
    private final Node head;

    public NonBlockingSet() {
        this.head = new Node(Integer.MIN_VALUE);
        this.head.next.set(new Node(Integer.MAX_VALUE), false);
    }

    @Override
    public boolean add(Integer x) {
        while (true) {
            final Window window = find(x);
            final Node pred = window.pred;
            final Node curr = window.curr;
            if (curr.key == x) {
                return false;
            }

            final Node newNode = new Node(x);
            newNode.next.set(curr, false);
            if (pred.next.compareAndSet(curr, newNode, false, false)) {
                return true;
            }
        }
    }

    @Override
    public boolean remove(Integer x) {
        while (true) {
            final Window window = find(x);
            final Node pred = window.pred;
            final Node curr = window.curr;
            if (curr.key != x) {
                return false;
            }

            final Node succ = curr.next.getReference();
            // Mark current as logically removed. Retry if unsuccessful (some other thread already marked it).
            boolean snip = curr.next.compareAndSet(succ, succ, false, true);
            if (!snip) {
                continue;
            }

            // Try once to physically remove the node (if no other thread already marked pred).
            // No need to retry, the garbage will be collected by another thread that finds in this region.
            pred.next.compareAndSet(curr, succ, false, false);
            return true;
        }
    }

    @Override
    public boolean contains(Integer x) {
        boolean[] marked = {false};
        Node curr = head;
        while (curr.key < x) {
            curr = curr.next.getReference();
            Node succ = curr.next.get(marked);
        }
        return curr.key == x && !marked[0];
    }

    private Window find(int x) {
        Node pred = null;
        Node curr = null;
        Node succ = null;
        boolean[] marked = {false};
        boolean snip;
        retry: while (true) {
            pred = head;
            curr = pred.next.getReference();
            while (true) {
                succ = curr.next.get(marked);
                while (marked[0]) {
                    snip = pred.next.compareAndSet(curr, succ, false, false);
                    if (!snip) {
                        continue retry;
                    }
                    curr = succ;
                    succ = curr.next.get(marked);
                }
                if (curr.key >= x) {
                    return new Window(pred, curr);
                }
                pred = curr;
                curr = succ;
            }
        }
    }
}
