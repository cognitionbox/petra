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
import io.cognitionbox.petra.util.IPMap;
import io.cognitionbox.petra.util.PCollection;
import io.cognitionbox.petra.util.Petra;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PMap<K,V> extends Identifyable implements IPMap<K,V>, Serializable, PCollection<Map.Entry<K,V>> {
  public PMap(){}
  public PMap(String description) {
    super(description);
  }

  public Map<K, V> getMap() {
    return map;
  }

  private Map<K,V> map = Petra.getFactory().createMap(getUniqueId());

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return map.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return map.containsValue(value);
  }

  @Override
  public V get(Object key) {
    return map.get(key);
  }

  @Override
  public V put(K key, V value) {
    return map.put(key, value);
  }

  @Override
  public V remove(Object key) {
    return map.remove(key);
  }

  @Override
  public void putAll( Map<? extends K, ? extends V> m) {
    map.putAll(m);
  }

  @Override
  public void clear() {
    map.clear();
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
  public boolean equals(Object o) {
    return map.equals(o);
  }

  @Override
  public int hashCode() {
    return map.hashCode();
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
  public V putIfAbsent(K key, V value) {
    return map.putIfAbsent(key, value);
  }

  @Override
  public boolean remove(Object key, Object value) {
    return map.remove(key, value);
  }

  @Override
  public boolean replace(K key, V oldValue, V newValue) {
    return map.replace(key, oldValue, newValue);
  }

  @Override
  public V replace(K key, V value) {
    return map.replace(key, value);
  }

  @Override
  public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
    return map.computeIfAbsent(key, mappingFunction);
  }

  @Override
  public V computeIfPresent(K key,
      BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
    return map.computeIfPresent(key, remappingFunction);
  }

  @Override
  public V compute(K key,
      BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
    return map.compute(key, remappingFunction);
  }

  @Override
  public V merge(K key, V value,
      BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
    return map.merge(key, value, remappingFunction);
  }
}
