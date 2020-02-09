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
package io.cognitionbox.petra.core.impl;

import io.cognitionbox.petra.core.IRingbuffer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MapBasedRingBuffer<T> implements IRingbuffer<T> {
    public MapBasedRingBuffer(String description, long capacity) {
        this.capacity = capacity;
        buffer = new ConcurrentHashMap<>();
        sequence = new AtomicLong(0);
    }

    private AtomicLong sequence;
    private Map<Long,T> buffer;
    private long capacity;

    public long add(T object){
        long seq = sequence.incrementAndGet();
        if(seq>=capacity){
            seq = 0;
            sequence.set(seq);
        }
        buffer.put(seq,object);
        return seq;
    }

    public T readOne(long seq){
        return buffer.get(seq);
    }

    @Override
    public long capacity() {
        return capacity;
    }

    @Override
    public long size() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long tailSequence() {
        return sequence.get();
    }

    @Override
    public long headSequence() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long remainingCapacity() {
        throw new UnsupportedOperationException();
    }
}
