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
import io.cognitionbox.petra.util.Petra;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.function.Supplier;

public class PStack<T> extends Identifyable implements Serializable {
    public PStack(){}
    public PStack(String id){super(id);}
    private transient Lock lock = Petra.getFactory().createLock(getUniqueId());
    private List<T> list = Petra.getFactory().createList(getUniqueId());
    public void push(T value) {
        tryLockThenRunnableFinallyUnlock(() -> {
            this.list.add(value);
            return null;
        });
    }
    public T pop(){
        return tryLockThenRunnableFinallyUnlock(() -> {
            if (!list.isEmpty()) {
                return list.remove(0);
            }
            return null;
        });
    }

    public T peek() {
        return tryLockThenRunnableFinallyUnlock(() -> {
            if (!list.isEmpty()) {
                return list.get(0);
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
