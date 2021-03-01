/**
 * Copyright 2016-2020 Aran Hakki
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cognitionbox.petra.util.impl;

import com.hazelcast.core.ISet;
import com.hazelcast.core.ItemListener;
import io.cognitionbox.petra.config.IPetraHazelcastConfig;
import io.cognitionbox.petra.core.impl.Identifyable;
import io.cognitionbox.petra.lang.RGraphComputer;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class HazelcastSetWrapper<T> extends Identifyable implements ISet<T>, Serializable {

    private transient ISet<T> set = ((IPetraHazelcastConfig) RGraphComputer.getConfig())
            .getHazelcastClient().getSet(getUniqueId());

    public HazelcastSetWrapper(String desc) {
        super(desc);
    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        set = ((IPetraHazelcastConfig) RGraphComputer.getConfig())
                .getHazelcastClient().getSet(getUniqueId());
    }

    public Set<T> getImdgSet() {
        return set;
    }

    @Override
    public int size() {
        return set.size();
    }

    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return set.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return set.iterator();
    }

    @Override
    public Object[] toArray() {
        return set.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return set.toArray(a);
    }

    @Override
    public boolean add(T t) {
        return set.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return set.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return set.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return set.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return set.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return set.removeAll(c);
    }

    @Override
    public void clear() {
        set.clear();
    }

    @Override
    public boolean equals(Object o) {
        return set.equals(o);
    }

    @Override
    public int hashCode() {
        return set.hashCode();
    }

    @Override
    public Spliterator<T> spliterator() {
        return set.spliterator();
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        return set.removeIf(filter);
    }

    @Override
    public Stream<T> stream() {
        return set.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return set.parallelStream();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        set.forEach(action);
    }

    @Override
    public String getName() {
        return set.getName();
    }

    @Override
    public String addItemListener(ItemListener<T> itemListener, boolean b) {
        return set.addItemListener(itemListener, b);
    }

    @Override
    public boolean removeItemListener(String s) {
        return set.removeItemListener(s);
    }

    @Override
    public String getPartitionKey() {
        return set.getPartitionKey();
    }

    @Override
    public String getServiceName() {
        return set.getServiceName();
    }

    @Override
    public void destroy() {
        set.destroy();
    }
}
