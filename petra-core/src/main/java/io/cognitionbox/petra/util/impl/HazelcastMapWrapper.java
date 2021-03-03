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

import com.hazelcast.aggregation.Aggregator;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.EntryView;
import com.hazelcast.core.ExecutionCallback;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.map.EntryProcessor;
import com.hazelcast.map.MapInterceptor;
import com.hazelcast.map.QueryCache;
import com.hazelcast.map.listener.MapListener;
import com.hazelcast.map.listener.MapPartitionLostListener;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.aggregation.Aggregation;
import com.hazelcast.mapreduce.aggregation.Supplier;
import com.hazelcast.monitor.LocalMapStats;
import com.hazelcast.projection.Projection;
import com.hazelcast.query.Predicate;
import io.cognitionbox.petra.core.impl.Identifyable;
import io.cognitionbox.petra.lang.RGraphComputer;
import io.cognitionbox.petra.lang.config.IPetraHazelcastConfig;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class HazelcastMapWrapper<K, V> extends Identifyable implements IMap<K, V>, Serializable {

    private transient IMap<K, V> map = ((IPetraHazelcastConfig) RGraphComputer.getConfig())
            .getHazelcastClient()
            .getMap(getUniqueId());

    public HazelcastMapWrapper(String name) {
        super(name);
    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        map = ((IPetraHazelcastConfig) RGraphComputer.getConfig())
                .getHazelcastClient()
                .getMap(getUniqueId());
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        this.map.putAll(map);
    }

    @Override
    public boolean containsKey(Object o) {
        return map.containsKey(o);
    }

    @Override
    public boolean containsValue(Object o) {
        return map.containsValue(o);
    }

    @Override
    public V get(Object o) {
        return map.get(o);
    }

    @Override
    public V put(K k, V v) {
        return map.put(k, v);
    }

    @Override
    public V remove(Object o) {
        return map.remove(o);
    }

    @Override
    public boolean remove(Object o, Object o1) {
        return map.remove(o, o1);
    }

    @Override
    public void removeAll(Predicate<K, V> predicate) {
        map.removeAll(predicate);
    }

    @Override
    public void delete(Object o) {
        map.delete(o);
    }

    @Override
    public void flush() {
        map.flush();
    }

    @Override
    public Map<K, V> getAll(Set<K> set) {
        return map.getAll(set);
    }

    @Override
    public void loadAll(boolean b) {
        map.loadAll(b);
    }

    @Override
    public void loadAll(Set<K> set, boolean b) {
        map.loadAll(set, b);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public ICompletableFuture<V> getAsync(K k) {
        return map.getAsync(k);
    }

    @Override
    public ICompletableFuture<V> putAsync(K k, V v) {
        return map.putAsync(k, v);
    }

    @Override
    public ICompletableFuture<V> putAsync(K k, V v, long l, TimeUnit timeUnit) {
        return map.putAsync(k, v, l, timeUnit);
    }

    @Override
    public ICompletableFuture<V> putAsync(K k, V v, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
        return map.putAsync(k, v, l, timeUnit, l1, timeUnit1);
    }

    @Override
    public ICompletableFuture<Void> setAsync(K k, V v) {
        return map.setAsync(k, v);
    }

    @Override
    public ICompletableFuture<Void> setAsync(K k, V v, long l, TimeUnit timeUnit) {
        return map.setAsync(k, v, l, timeUnit);
    }

    @Override
    public ICompletableFuture<Void> setAsync(K k, V v, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
        return map.setAsync(k, v, l, timeUnit, l1, timeUnit1);
    }

    @Override
    public ICompletableFuture<V> removeAsync(K k) {
        return map.removeAsync(k);
    }

    @Override
    public boolean tryRemove(K k, long l, TimeUnit timeUnit) {
        return map.tryRemove(k, l, timeUnit);
    }

    @Override
    public boolean tryPut(K k, V v, long l, TimeUnit timeUnit) {
        return map.tryPut(k, v, l, timeUnit);
    }

    @Override
    public V put(K k, V v, long l, TimeUnit timeUnit) {
        return map.put(k, v, l, timeUnit);
    }

    @Override
    public V put(K k, V v, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
        return map.put(k, v, l, timeUnit, l1, timeUnit1);
    }

    @Override
    public void putTransient(K k, V v, long l, TimeUnit timeUnit) {
        map.putTransient(k, v, l, timeUnit);
    }

    @Override
    public void putTransient(K k, V v, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
        map.putTransient(k, v, l, timeUnit, l1, timeUnit1);
    }

    @Override
    public V putIfAbsent(K k, V v) {
        return map.putIfAbsent(k, v);
    }

    @Override
    public V putIfAbsent(K k, V v, long l, TimeUnit timeUnit) {
        return map.putIfAbsent(k, v, l, timeUnit);
    }

    @Override
    public V putIfAbsent(K k, V v, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
        return map.putIfAbsent(k, v, l, timeUnit, l1, timeUnit1);
    }

    @Override
    public boolean replace(K k, V v, V v1) {
        return map.replace(k, v, v1);
    }

    @Override
    public V replace(K k, V v) {
        return map.replace(k, v);
    }

    @Override
    public void set(K k, V v) {
        map.set(k, v);
    }

    @Override
    public void set(K k, V v, long l, TimeUnit timeUnit) {
        map.set(k, v, l, timeUnit);
    }

    @Override
    public void set(K k, V v, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
        map.set(k, v, l, timeUnit, l1, timeUnit1);
    }

    @Override
    public void lock(K k) {
        map.lock(k);
    }

    @Override
    public void lock(K k, long l, TimeUnit timeUnit) {
        map.lock(k, l, timeUnit);
    }

    @Override
    public boolean isLocked(K k) {
        return map.isLocked(k);
    }

    @Override
    public boolean tryLock(K k) {
        return map.tryLock(k);
    }

    @Override
    public boolean tryLock(K k, long l, TimeUnit timeUnit) throws InterruptedException {
        return map.tryLock(k, l, timeUnit);
    }

    @Override
    public boolean tryLock(K k, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) throws InterruptedException {
        return map.tryLock(k, l, timeUnit, l1, timeUnit1);
    }

    @Override
    public void unlock(K k) {
        map.unlock(k);
    }

    @Override
    public void forceUnlock(K k) {
        map.forceUnlock(k);
    }

    @Override
    public String addLocalEntryListener(MapListener mapListener) {
        return map.addLocalEntryListener(mapListener);
    }

    @Override
    public String addLocalEntryListener(EntryListener entryListener) {
        return map.addLocalEntryListener(entryListener);
    }

    @Override
    public String addLocalEntryListener(MapListener mapListener, Predicate<K, V> predicate, boolean b) {
        return map.addLocalEntryListener(mapListener, predicate, b);
    }

    @Override
    public String addLocalEntryListener(EntryListener entryListener, Predicate<K, V> predicate, boolean b) {
        return map.addLocalEntryListener(entryListener, predicate, b);
    }

    @Override
    public String addLocalEntryListener(MapListener mapListener, Predicate<K, V> predicate, K k, boolean b) {
        return map.addLocalEntryListener(mapListener, predicate, k, b);
    }

    @Override
    public String addLocalEntryListener(EntryListener entryListener, Predicate<K, V> predicate, K k, boolean b) {
        return map.addLocalEntryListener(entryListener, predicate, k, b);
    }

    @Override
    public String addInterceptor(MapInterceptor mapInterceptor) {
        return map.addInterceptor(mapInterceptor);
    }

    @Override
    public void removeInterceptor(String s) {
        map.removeInterceptor(s);
    }

    @Override
    public String addEntryListener(MapListener mapListener, boolean b) {
        return map.addEntryListener(mapListener, b);
    }

    @Override
    public String addEntryListener(EntryListener entryListener, boolean b) {
        return map.addEntryListener(entryListener, b);
    }

    @Override
    public boolean removeEntryListener(String s) {
        return map.removeEntryListener(s);
    }

    @Override
    public String addPartitionLostListener(MapPartitionLostListener mapPartitionLostListener) {
        return map.addPartitionLostListener(mapPartitionLostListener);
    }

    @Override
    public boolean removePartitionLostListener(String s) {
        return map.removePartitionLostListener(s);
    }

    @Override
    public String addEntryListener(MapListener mapListener, K k, boolean b) {
        return map.addEntryListener(mapListener, k, b);
    }

    @Override
    public String addEntryListener(EntryListener entryListener, K k, boolean b) {
        return map.addEntryListener(entryListener, k, b);
    }

    @Override
    public String addEntryListener(MapListener mapListener, Predicate<K, V> predicate, boolean b) {
        return map.addEntryListener(mapListener, predicate, b);
    }

    @Override
    public String addEntryListener(EntryListener entryListener, Predicate<K, V> predicate, boolean b) {
        return map.addEntryListener(entryListener, predicate, b);
    }

    @Override
    public String addEntryListener(MapListener mapListener, Predicate<K, V> predicate, K k, boolean b) {
        return map.addEntryListener(mapListener, predicate, k, b);
    }

    @Override
    public String addEntryListener(EntryListener entryListener, Predicate<K, V> predicate, K k, boolean b) {
        return map.addEntryListener(entryListener, predicate, k, b);
    }

    @Override
    public EntryView<K, V> getEntryView(K k) {
        return map.getEntryView(k);
    }

    @Override
    public boolean evict(K k) {
        return map.evict(k);
    }

    @Override
    public void evictAll() {
        map.evictAll();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    @Override
    public Set<K> keySet(Predicate predicate) {
        return map.keySet(predicate);
    }

    @Override
    public Set<Entry<K, V>> entrySet(Predicate predicate) {
        return map.entrySet(predicate);
    }

    @Override
    public Collection<V> values(Predicate predicate) {
        return map.values(predicate);
    }

    @Override
    public Set<K> localKeySet() {
        return map.localKeySet();
    }

    @Override
    public Set<K> localKeySet(Predicate predicate) {
        return map.localKeySet(predicate);
    }

    @Override
    public void addIndex(String s, boolean b) {
        map.addIndex(s, b);
    }

    @Override
    public LocalMapStats getLocalMapStats() {
        return map.getLocalMapStats();
    }

    @Override
    public Object executeOnKey(K k, EntryProcessor entryProcessor) {
        return map.executeOnKey(k, entryProcessor);
    }

    @Override
    public Map<K, Object> executeOnKeys(Set<K> set, EntryProcessor entryProcessor) {
        return map.executeOnKeys(set, entryProcessor);
    }

    @Override
    public void submitToKey(K k, EntryProcessor entryProcessor, ExecutionCallback executionCallback) {
        map.submitToKey(k, entryProcessor, executionCallback);
    }

    @Override
    public ICompletableFuture submitToKey(K k, EntryProcessor entryProcessor) {
        return map.submitToKey(k, entryProcessor);
    }

    @Override
    public Map<K, Object> executeOnEntries(EntryProcessor entryProcessor) {
        return map.executeOnEntries(entryProcessor);
    }

    @Override
    public Map<K, Object> executeOnEntries(EntryProcessor entryProcessor, Predicate predicate) {
        return map.executeOnEntries(entryProcessor, predicate);
    }

    @Override
    public <R> R aggregate(Aggregator<Entry<K, V>, R> aggregator) {
        return map.aggregate(aggregator);
    }

    @Override
    public <R> R aggregate(Aggregator<Entry<K, V>, R> aggregator, Predicate<K, V> predicate) {
        return map.aggregate(aggregator, predicate);
    }

    @Override
    public <R> Collection<R> project(Projection<Entry<K, V>, R> projection) {
        return map.project(projection);
    }

    @Override
    public <R> Collection<R> project(Projection<Entry<K, V>, R> projection, Predicate<K, V> predicate) {
        return map.project(projection, predicate);
    }

    @Override
    @Deprecated
    public <SuppliedValue, Result> Result aggregate(Supplier<K, V, SuppliedValue> supplier, Aggregation<K, SuppliedValue, Result> aggregation) {
        return map.aggregate(supplier, aggregation);
    }

    @Override
    @Deprecated
    public <SuppliedValue, Result> Result aggregate(Supplier<K, V, SuppliedValue> supplier, Aggregation<K, SuppliedValue, Result> aggregation, JobTracker jobTracker) {
        return map.aggregate(supplier, aggregation, jobTracker);
    }

    @Override
    public QueryCache<K, V> getQueryCache(String s) {
        return map.getQueryCache(s);
    }

    @Override
    public QueryCache<K, V> getQueryCache(String s, Predicate<K, V> predicate, boolean b) {
        return map.getQueryCache(s, predicate, b);
    }

    @Override
    public QueryCache<K, V> getQueryCache(String s, MapListener mapListener, Predicate<K, V> predicate, boolean b) {
        return map.getQueryCache(s, mapListener, predicate, b);
    }

    @Override
    public boolean setTtl(K k, long l, TimeUnit timeUnit) {
        return map.setTtl(k, l, timeUnit);
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return map.getOrDefault(key, defaultValue);
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        map.forEach(action);
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        map.replaceAll(function);
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return map.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return map.computeIfPresent(key, remappingFunction);
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return map.compute(key, remappingFunction);
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return map.merge(key, value, remappingFunction);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        return map.equals(o);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public String getPartitionKey() {
        return map.getPartitionKey();
    }

    @Override
    public String getName() {
        return map.getName();
    }

    @Override
    public String getServiceName() {
        return map.getServiceName();
    }

    @Override
    public void destroy() {
        map.destroy();
    }
}
