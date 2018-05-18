package me.ivanyu.concurrent_algorithms;

import me.ivanyu.concurrent_algorithms.set.Set;
import me.ivanyu.concurrent_algorithms.set.coarse.CoarseGrainedSyncSet;
import me.ivanyu.concurrent_algorithms.set.fine.FineGrainedSyncSet;
import me.ivanyu.concurrent_algorithms.set.lazy.LazySyncSet;
import me.ivanyu.concurrent_algorithms.set.nonblocking.NonBlockingSet;
import me.ivanyu.concurrent_algorithms.set.optimistic.OptimisticSyncSet;

public class App {
    public static void main(String[] args) {
//        final Set<Integer> set = new CoarseGrainedSyncSet();
//        final Set<Integer> set = new FineGrainedSyncSet();
//        final Set<Integer> set = new OptimisticSyncSet();
//        final Set<Integer> set = new LazySyncSet();
        final Set<Integer> set = new NonBlockingSet();
        assert !set.contains(2);
        assert !set.remove(2);

        assert set.add(5);
        assert set.add(10);
        assert set.add(1);
        assert set.add(12);
        assert set.add(2);

        assert set.contains(2);
        assert set.remove(2);
        assert !set.contains(2);

        assert set.contains(5);
        assert set.remove(5);
        assert !set.contains(5);

        assert set.contains(12);
        assert set.remove(12);
        assert !set.contains(12);

        assert set.contains(1);
        assert set.remove(1);
        assert !set.contains(1);

        assert set.contains(10);
        assert set.remove(10);
        assert !set.contains(10);
    }
}
