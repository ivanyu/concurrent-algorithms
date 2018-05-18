package me.ivanyu.concurrent_algorithms.set.nonblocking;

class Window {
    public final Node pred;
    public final Node curr;

    Window(final Node pred, final Node curr) {
        this.pred = pred;
        this.curr = curr;
    }
}
