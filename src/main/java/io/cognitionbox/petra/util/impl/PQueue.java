/**
 * Copyright (C) 2016-2020 Aran Hakki.
 *
 * This file is part of Petra.
 *
 * Petra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Petra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Petra.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.cognitionbox.petra.util.impl;

import io.cognitionbox.petra.core.impl.Identifyable;
import io.cognitionbox.petra.lang.Void;
import io.cognitionbox.petra.util.Petra;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.function.Supplier;

public class PQueue<T> extends Identifyable implements Queue<T>, Serializable {
    public PQueue(){}
    public PQueue(String description) {
        super(description);
    }

    private List<T> list = Petra.getFactory().createList(getUniqueId());
    public List<T> getList() {
        return list;
    }

    private transient Lock lock = Petra.getFactory().createLock(getUniqueId());

    private static class DummyLock implements Lock {

        @Override
        public void lock() {

        }

        @Override
        public void lockInterruptibly() throws InterruptedException {

        }

        @Override
        public boolean tryLock() {
            return false;
        }

        @Override
        public boolean tryLock(long time,  TimeUnit unit) throws InterruptedException {
            return false;
        }

        @Override
        public void unlock() {

        }


        @Override
        public Condition newCondition() {
            return null;
        }
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }


    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }


    @Override
    public Object[] toArray() {
        return list.toArray();
    }


    @Override
    public <T1> T1[] toArray( T1[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(T t) {
        return tryLockThenRunnableFinallyUnlock(() -> {
            return list.add(t);
        });
    }

    @Override
    public boolean remove(Object o) {
        return tryLockThenRunnableFinallyUnlock(() -> {
            return list.remove(o);
        });
    }

    @Override
    public boolean containsAll( Collection<?> c) {
        return tryLockThenRunnableFinallyUnlock(() -> {
            return list.containsAll(c);
        });
    }

    @Override
    public boolean addAll( Collection<? extends T> c) {
        return tryLockThenRunnableFinallyUnlock(() -> {
            return list.addAll(c);
        });
    }

    @Override
    public boolean removeAll( Collection<?> c) {
        return tryLockThenRunnableFinallyUnlock(() -> {
            return list.removeAll(c);
        });
    }

    @Override
    public boolean retainAll( Collection<?> c) {
        return tryLockThenRunnableFinallyUnlock(() -> {
            return list.retainAll(c);
        });
    }

    @Override
    public void clear() {
        tryLockThenRunnableFinallyUnlock(() -> {
            list.clear();
            return Void.vd;
        });
    }

    @Override
    public boolean offer(T t) {
        return tryLockThenRunnableFinallyUnlock(() -> {
            return list.add(t);
        });
    }

    @Override
    public T remove() {
        return tryLockThenRunnableFinallyUnlock(() -> {
            T removed = poll();
            if (removed != null) {
                return removed;
            }
            throw new NoSuchElementException();
        });
    }

    @Override
    public T poll() {
        return tryLockThenRunnableFinallyUnlock(() -> {
            T peek = peek();
            if (peek != null) {
                remove(peek);
            }
            return peek;
        });
    }

    @Override
    public T element() {
        return tryLockThenRunnableFinallyUnlock(() -> {
            T peek = peek();
            if (peek != null) {
                return peek;
            }
            throw new NoSuchElementException();
        });
    }

    @Override
    public T peek() {
        return tryLockThenRunnableFinallyUnlock(() -> {
            if (!isEmpty()) {
                return list.get(size() - 1);
            }
            return null;
        });
    }

    private <T> T tryLockThenRunnableFinallyUnlock(Supplier<T> supplier) {
        try {
            lock.lock();
            return supplier.get();
        } finally {
            lock.unlock();
        }
    }
}
