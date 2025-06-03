package org.springfeed.newsfeed.global.jwt.blacklist;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class SizedConcurrentSet<E> {

    private final int maxSize;
    private final Set<E> set = new HashSet<>();

    public SizedConcurrentSet(int maxSize) {
        this.maxSize = maxSize;
    }

    public synchronized boolean add(E element) {
        if (set.contains(element)) {
            return false;
        }

        if (set.size() >= maxSize) {
            throw new RuntimeException("Set has reached its max capacity");
        }

        return set.add(element);
    }

    public synchronized boolean remove(E element) {
        return set.remove(element);
    }

    public synchronized boolean contains(E element) {
        return set.contains(element);
    }

    public synchronized int size() {
        return set.size();
    }
}