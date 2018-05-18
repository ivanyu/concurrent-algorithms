package me.ivanyu.concurrent_algorithms.set;

public interface Set<T> {
    boolean add(T x);
    boolean remove(T x);
    boolean contains(T x);
}
