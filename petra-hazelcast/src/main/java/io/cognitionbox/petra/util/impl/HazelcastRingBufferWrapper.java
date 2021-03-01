/**
 * Copyright 2016-2020 Aran Hakki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cognitionbox.petra.util.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IFunction;
import com.hazelcast.ringbuffer.OverflowPolicy;
import com.hazelcast.ringbuffer.ReadResultSet;
import com.hazelcast.ringbuffer.Ringbuffer;
import io.cognitionbox.petra.config.IPetraHazelcastConfig;
import io.cognitionbox.petra.config.PetraHazelcastConfig;
import io.cognitionbox.petra.core.IRingbuffer;
import io.cognitionbox.petra.core.impl.Identifyable;
import io.cognitionbox.petra.lang.RGraphComputer;
import io.cognitionbox.petra.util.Petra;

import java.io.IOException;
import java.util.Collection;
import java.util.function.Supplier;

public class HazelcastRingBufferWrapper<T> extends Identifyable implements IRingbuffer<T>, Ringbuffer<T> {
    transient private Ringbuffer<T> ringbuffer = ((IPetraHazelcastConfig)RGraphComputer.getConfig())
            .getHazelcastClient().getRingbuffer(getUniqueId());

    public HazelcastRingBufferWrapper(String name) {
        super(name);
    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        ringbuffer = ((IPetraHazelcastConfig)RGraphComputer.getConfig())
                .getHazelcastClient().getRingbuffer(getUniqueId());
    }

    @Override
    public long capacity() {
        return ringbuffer.capacity();
    }

    @Override
    public long size() {
        return ringbuffer.size();
    }

    @Override
    public long tailSequence() {
        return ringbuffer.tailSequence();
    }

    @Override
    public long headSequence() {
        return ringbuffer.headSequence();
    }

    @Override
    public long remainingCapacity() {
        return ringbuffer.remainingCapacity();
    }

    @Override
    public long add(T t) {
        return ringbuffer.add(t);
    }

    public ICompletableFuture<Long> addAsync(T t, OverflowPolicy overflowPolicy) {
        return ringbuffer.addAsync(t, overflowPolicy);
    }

    @Override
    public T readOne(long l) throws InterruptedException {
        return ringbuffer.readOne(l);
    }

    public ICompletableFuture<Long> addAllAsync(Collection<? extends T> collection, OverflowPolicy overflowPolicy) {
        return ringbuffer.addAllAsync(collection, overflowPolicy);
    }

    public ICompletableFuture<ReadResultSet<T>> readManyAsync(long l, int i, int i1, IFunction<T, Boolean> iFunction) {
        return ringbuffer.readManyAsync(l, i, i1, iFunction);
    }

    @Override
    public String getPartitionKey() {
        return ringbuffer.getPartitionKey();
    }

    @Override
    public String getName() {
        return ringbuffer.getName();
    }

    @Override
    public String getServiceName() {
        return ringbuffer.getServiceName();
    }

    @Override
    public void destroy() {
        ringbuffer.destroy();
    }
}
