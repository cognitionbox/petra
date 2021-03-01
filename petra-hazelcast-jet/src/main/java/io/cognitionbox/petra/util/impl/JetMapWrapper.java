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
import com.hazelcast.jet.JetInstance;
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
import com.hazelcast.spi.annotation.Beta;
import io.cognitionbox.petra.core.impl.Identifyable;
import io.cognitionbox.petra.util.function.ISupplier;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class JetMapWrapper<K, V> extends Identifyable implements IMap<K, V>, Serializable {

    public IMap<K, V> getJetMap() {
        return jetMap;
    }

    private ISupplier<JetInstance> jetClientSupplier;
    private transient IMap<K, V> jetMap = jetClientSupplier.get().getMap(getUniqueId());

    public JetMapWrapper(String name, ISupplier<JetInstance> hazelcastClientSupplier) {
        super(name);
        this.jetClientSupplier = jetClientSupplier;
    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        jetMap = jetClientSupplier.get().getMap(getUniqueId());
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        jetMap.putAll(map);
    }

    @Override
    public boolean containsKey(Object o) {
        return jetMap.containsKey(o);
    }

    @Override
    public boolean containsValue(Object o) {
        return jetMap.containsValue(o);
    }

    @Override
    public V get(Object o) {
        return jetMap.get(o);
    }

    @Override
    public V put(K k, V v) {
        return jetMap.put(k, v);
    }

    @Override
    public V remove(Object o) {
        return jetMap.remove(o);
    }

    @Override
    public boolean remove(Object o, Object o1) {
        return jetMap.remove(o, o1);
    }

    @Override
    public void removeAll(Predicate<K, V> predicate) {
        jetMap.removeAll(predicate);
    }

    @Override
    public void delete(Object o) {
        jetMap.delete(o);
    }

    @Override
    public void flush() {
        jetMap.flush();
    }

    @Override
    public Map<K, V> getAll(Set<K> set) {
        return jetMap.getAll(set);
    }

    @Override
    public void loadAll(boolean b) {
        jetMap.loadAll(b);
    }

    @Override
    public void loadAll(Set<K> set, boolean b) {
        jetMap.loadAll(set, b);
    }

    @Override
    public void clear() {
        jetMap.clear();
    }

    @Override
    public ICompletableFuture<V> getAsync(K k) {
        return jetMap.getAsync(k);
    }

    @Override
    public ICompletableFuture<V> putAsync(K k, V v) {
        return jetMap.putAsync(k, v);
    }

    @Override
    public ICompletableFuture<V> putAsync(K k, V v, long l,
                                          TimeUnit timeUnit) {
        return jetMap.putAsync(k, v, l, timeUnit);
    }

    @Override
    public ICompletableFuture<V> putAsync(K k, V v, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
        return null;
    }

    @Override
    public ICompletableFuture<Void> setAsync(K k, V v) {
        return jetMap.setAsync(k, v);
    }

    @Override
    public ICompletableFuture<Void> setAsync(K k, V v, long l,
                                             TimeUnit timeUnit) {
        return jetMap.setAsync(k, v, l, timeUnit);
    }

    @Override
    public ICompletableFuture<Void> setAsync(K k, V v, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
        return null;
    }

    @Override
    public ICompletableFuture<V> removeAsync(K k) {
        return jetMap.removeAsync(k);
    }

    @Override
    public boolean tryRemove(K k, long l, TimeUnit timeUnit) {
        return jetMap.tryRemove(k, l, timeUnit);
    }

    @Override
    public boolean tryPut(K k, V v, long l, TimeUnit timeUnit) {
        return jetMap.tryPut(k, v, l, timeUnit);
    }

    @Override
    public V put(K k, V v, long l, TimeUnit timeUnit) {
        return jetMap.put(k, v, l, timeUnit);
    }

    @Override
    public V put(K k, V v, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
        return null;
    }

    @Override
    public void putTransient(K k, V v, long l, TimeUnit timeUnit) {
        jetMap.putTransient(k, v, l, timeUnit);
    }

    @Override
    public void putTransient(K k, V v, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {

    }

    @Override
    public V putIfAbsent(K k, V v) {
        return jetMap.putIfAbsent(k, v);
    }

    @Override
    public V putIfAbsent(K k, V v, long l, TimeUnit timeUnit) {
        return jetMap.putIfAbsent(k, v, l, timeUnit);
    }

    @Override
    public V putIfAbsent(K k, V v, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {
        return null;
    }

    @Override
    public boolean replace(K k, V v, V v1) {
        return jetMap.replace(k, v, v1);
    }

    @Override
    public V replace(K k, V v) {
        return jetMap.replace(k, v);
    }

    @Override
    public void set(K k, V v) {
        jetMap.set(k, v);
    }

    @Override
    public void set(K k, V v, long l, TimeUnit timeUnit) {
        jetMap.set(k, v, l, timeUnit);
    }

    @Override
    public void set(K k, V v, long l, TimeUnit timeUnit, long l1, TimeUnit timeUnit1) {

    }

    @Override
    public void lock(K k) {
        jetMap.lock(k);
    }

    @Override
    public void lock(K k, long l, TimeUnit timeUnit) {
        jetMap.lock(k, l, timeUnit);
    }

    @Override
    public boolean isLocked(K k) {
        return jetMap.isLocked(k);
    }

    @Override
    public boolean tryLock(K k) {
        return jetMap.tryLock(k);
    }

    @Override
    public boolean tryLock(K k, long l, TimeUnit timeUnit) throws InterruptedException {
        return jetMap.tryLock(k, l, timeUnit);
    }

    @Override
    public boolean tryLock(K k, long l, TimeUnit timeUnit, long l1,
                           TimeUnit timeUnit1) throws InterruptedException {
        return jetMap.tryLock(k, l, timeUnit, l1, timeUnit1);
    }

    @Override
    public void unlock(K k) {
        jetMap.unlock(k);
    }

    @Override
    public void forceUnlock(K k) {
        jetMap.forceUnlock(k);
    }

    @Override
    public String addLocalEntryListener(MapListener mapListener) {
        return jetMap.addLocalEntryListener(mapListener);
    }

    @Override
    public String addLocalEntryListener(EntryListener entryListener) {
        return jetMap.addLocalEntryListener(entryListener);
    }

    @Override
    public String addLocalEntryListener(MapListener mapListener,
                                        Predicate<K, V> predicate, boolean b) {
        return jetMap.addLocalEntryListener(mapListener, predicate, b);
    }

    @Override
    public String addLocalEntryListener(EntryListener entryListener,
                                        Predicate<K, V> predicate, boolean b) {
        return jetMap.addLocalEntryListener(entryListener, predicate, b);
    }

    @Override
    public String addLocalEntryListener(MapListener mapListener,
                                        Predicate<K, V> predicate, K k, boolean b) {
        return jetMap.addLocalEntryListener(mapListener, predicate, k, b);
    }

    @Override
    public String addLocalEntryListener(EntryListener entryListener,
                                        Predicate<K, V> predicate, K k, boolean b) {
        return jetMap.addLocalEntryListener(entryListener, predicate, k, b);
    }

    @Override
    public String addInterceptor(MapInterceptor mapInterceptor) {
        return jetMap.addInterceptor(mapInterceptor);
    }

    @Override
    public void removeInterceptor(String s) {
        jetMap.removeInterceptor(s);
    }

    @Override
    public String addEntryListener(MapListener mapListener, boolean b) {
        return jetMap.addEntryListener(mapListener, b);
    }

    @Override
    public String addEntryListener(EntryListener entryListener, boolean b) {
        return jetMap.addEntryListener(entryListener, b);
    }

    @Override
    public boolean removeEntryListener(String s) {
        return jetMap.removeEntryListener(s);
    }

    @Override
    public String addPartitionLostListener(
            MapPartitionLostListener mapPartitionLostListener) {
        return jetMap.addPartitionLostListener(mapPartitionLostListener);
    }

    @Override
    public boolean removePartitionLostListener(String s) {
        return jetMap.removePartitionLostListener(s);
    }

    @Override
    public String addEntryListener(MapListener mapListener, K k, boolean b) {
        return jetMap.addEntryListener(mapListener, k, b);
    }

    @Override
    public String addEntryListener(EntryListener entryListener, K k, boolean b) {
        return jetMap.addEntryListener(entryListener, k, b);
    }

    @Override
    public String addEntryListener(MapListener mapListener,
                                   Predicate<K, V> predicate, boolean b) {
        return jetMap.addEntryListener(mapListener, predicate, b);
    }

    @Override
    public String addEntryListener(EntryListener entryListener,
                                   Predicate<K, V> predicate, boolean b) {
        return jetMap.addEntryListener(entryListener, predicate, b);
    }

    @Override
    public String addEntryListener(MapListener mapListener,
                                   Predicate<K, V> predicate, K k, boolean b) {
        return jetMap.addEntryListener(mapListener, predicate, k, b);
    }

    @Override
    public String addEntryListener(EntryListener entryListener,
                                   Predicate<K, V> predicate, K k, boolean b) {
        return jetMap.addEntryListener(entryListener, predicate, k, b);
    }

    @Override
    public EntryView<K, V> getEntryView(K k) {
        return jetMap.getEntryView(k);
    }

    @Override
    public boolean evict(K k) {
        return jetMap.evict(k);
    }

    @Override
    public void evictAll() {
        jetMap.evictAll();
    }

    @Override
    public Set<K> keySet() {
        return jetMap.keySet();
    }

    @Override
    public Collection<V> values() {
        return jetMap.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return jetMap.entrySet();
    }

    @Override
    public Set<K> keySet(Predicate predicate) {
        return jetMap.keySet(predicate);
    }

    @Override
    public Set<Entry<K, V>> entrySet(Predicate predicate) {
        return jetMap.entrySet(predicate);
    }

    @Override
    public Collection<V> values(Predicate predicate) {
        return jetMap.values(predicate);
    }

    @Override
    public Set<K> localKeySet() {
        return jetMap.localKeySet();
    }

    @Override
    public Set<K> localKeySet(Predicate predicate) {
        return jetMap.localKeySet(predicate);
    }

    @Override
    public void addIndex(String s, boolean b) {
        jetMap.addIndex(s, b);
    }

    @Override
    public LocalMapStats getLocalMapStats() {
        return jetMap.getLocalMapStats();
    }

    @Override
    public Object executeOnKey(K k, EntryProcessor entryProcessor) {
        return jetMap.executeOnKey(k, entryProcessor);
    }

    @Override
    public Map<K, Object> executeOnKeys(Set<K> set,
                                        EntryProcessor entryProcessor) {
        return jetMap.executeOnKeys(set, entryProcessor);
    }

    @Override
    public void submitToKey(K k, EntryProcessor entryProcessor,
                            ExecutionCallback executionCallback) {
        jetMap.submitToKey(k, entryProcessor, executionCallback);
    }

    @Override
    public ICompletableFuture submitToKey(K k,
                                          EntryProcessor entryProcessor) {
        return jetMap.submitToKey(k, entryProcessor);
    }

    @Override
    public Map<K, Object> executeOnEntries(EntryProcessor entryProcessor) {
        return jetMap.executeOnEntries(entryProcessor);
    }

    @Override
    public Map<K, Object> executeOnEntries(EntryProcessor entryProcessor,
                                           Predicate predicate) {
        return jetMap.executeOnEntries(entryProcessor, predicate);
    }

    @Override
    public <R> R aggregate(Aggregator<Entry<K, V>, R> aggregator) {
        return jetMap.aggregate(aggregator);
    }

    @Override
    public <R> R aggregate(Aggregator<Entry<K, V>, R> aggregator,
                           Predicate<K, V> predicate) {
        return jetMap.aggregate(aggregator, predicate);
    }

    @Override
    public <R> Collection<R> project(
            Projection<Entry<K, V>, R> projection) {
        return jetMap.project(projection);
    }

    @Override
    public <R> Collection<R> project(
            Projection<Entry<K, V>, R> projection,
            Predicate<K, V> predicate) {
        return jetMap.project(projection, predicate);
    }

    @Override
    @Deprecated
    public <SuppliedValue, Result> Result aggregate(
            Supplier<K, V, SuppliedValue> supplier,
            Aggregation<K, SuppliedValue, Result> aggregation) {
        return jetMap.aggregate(supplier, aggregation);
    }

    @Override
    @Deprecated
    public <SuppliedValue, Result> Result aggregate(
            Supplier<K, V, SuppliedValue> supplier,
            Aggregation<K, SuppliedValue, Result> aggregation,
            JobTracker jobTracker) {
        return jetMap.aggregate(supplier, aggregation, jobTracker);
    }

    @Override
    @Beta
    public QueryCache<K, V> getQueryCache(String s) {
        return jetMap.getQueryCache(s);
    }

    @Override
    @Beta
    public QueryCache<K, V> getQueryCache(String s,
                                          Predicate<K, V> predicate, boolean b) {
        return jetMap.getQueryCache(s, predicate, b);
    }

    @Override
    @Beta
    public QueryCache<K, V> getQueryCache(String s,
                                          MapListener mapListener,
                                          Predicate<K, V> predicate, boolean b) {
        return jetMap.getQueryCache(s, mapListener, predicate, b);
    }

    @Override
    public boolean setTtl(K k, long l, TimeUnit timeUnit) {
        return false;
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return jetMap.getOrDefault(key, defaultValue);
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        jetMap.forEach(action);
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        jetMap.replaceAll(function);
    }

    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return jetMap.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public V computeIfPresent(K key,
                              BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return jetMap.computeIfPresent(key, remappingFunction);
    }

    @Override
    public V compute(K key,
                     BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return jetMap.compute(key, remappingFunction);
    }

    @Override
    public V merge(K key, V value,
                   BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return jetMap.merge(key, value, remappingFunction);
    }

    @Override
    public int size() {
        return jetMap.size();
    }

    @Override
    public boolean isEmpty() {
        return jetMap.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        return jetMap.equals(o);
    }

    @Override
    public int hashCode() {
        return jetMap.hashCode();
    }

    @Override
    public String getPartitionKey() {
        return jetMap.getPartitionKey();
    }

    @Override
    public String getName() {
        return jetMap.getName();
    }

    @Override
    public String getServiceName() {
        return jetMap.getServiceName();
    }

    @Override
    public void destroy() {
        jetMap.destroy();
    }
}
