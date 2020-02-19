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
import com.hazelcast.core.IList;
import com.hazelcast.core.ItemListener;
import io.cognitionbox.petra.config.PetraHazelcastConfig;
import io.cognitionbox.petra.config.PetraJetConfig;
import io.cognitionbox.petra.core.impl.Identifyable;
import io.cognitionbox.petra.lang.RGraphComputer;
import io.cognitionbox.petra.util.IPListJet;
import io.cognitionbox.petra.util.function.ISupplier;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public final class JetListWrapper<T> extends Identifyable implements IPListJet<T>, Serializable {

    private ISupplier<HazelcastInstance> hazelcastClientSupplier;
    private transient IList<T> jetList = ((PetraJetConfig) RGraphComputer.getConfig())
            .getHazelcastClient()
            .getList(getUniqueId());
    public JetListWrapper(String name, ISupplier<HazelcastInstance> hazelcastClientSupplier){
        super(name);
        this.hazelcastClientSupplier = hazelcastClientSupplier;
    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        jetList = hazelcastClientSupplier.get().getList(getUniqueId());
    }

    public IList<T> getJetList() {
        return jetList;
    }

    @Override
    public int size() {
        return jetList.size();
    }

    @Override
    public boolean isEmpty() {
        return jetList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return jetList.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return jetList.iterator();
    }

    @Override
    public Object[] toArray() {
        return jetList.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return jetList.toArray(a);
    }

    @Override
    public boolean add(T t) {
        return jetList.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return jetList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return jetList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return jetList.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return jetList.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return jetList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return jetList.retainAll(c);
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {
        jetList.replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super T> c) {
        jetList.sort(c);
    }

    @Override
    public void clear() {
        jetList.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof List) {
            // will equals be affected by race conditions?
            // they should not, equalities happen at pre/post condition checks and if within a computation
            // they should happen within queue actors operations or on on the mutually exclusive objects,
            // this is enforced by a combination of the XOR check and by developer convention
            return jetList.containsAll((Collection<?>) o);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return jetList.hashCode();
    }

    @Override
    public T get(int index) {
        return jetList.get(index);
    }

    @Override
    public T set(int index, T element) {
        return jetList.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        jetList.add(index, element);
    }

    @Override
    public T remove(int index) {
        return jetList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return jetList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return jetList.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return jetList.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return jetList.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return jetList.subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator<T> spliterator() {
        return jetList.spliterator();
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        return jetList.removeIf(filter);
    }

    @Override
    public Stream<T> stream() {
        return jetList.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return jetList.stream();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        jetList.forEach(action);
    }

    @Override
    public String getName() {
        return jetList.getName();
    }

    @Override
    public String addItemListener(ItemListener<T> itemListener, boolean b) {
        return jetList.addItemListener(itemListener, b);
    }

    @Override
    public boolean removeItemListener(String s) {
        return jetList.removeItemListener(s);
    }

    @Override
    public String getPartitionKey() {
        return jetList.getPartitionKey();
    }

    @Override
    public String getServiceName() {
        return jetList.getServiceName();
    }

    @Override
    public void destroy() {
        jetList.destroy();
    }
}
