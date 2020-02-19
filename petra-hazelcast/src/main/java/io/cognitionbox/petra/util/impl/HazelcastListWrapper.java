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
import io.cognitionbox.petra.config.IPetraHazelcastConfig;
import io.cognitionbox.petra.config.PetraHazelcastConfig;
import io.cognitionbox.petra.core.impl.Identifyable;
import io.cognitionbox.petra.lang.RGraphComputer;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public final class HazelcastListWrapper<T> extends Identifyable implements IList<T>, Serializable {

  private transient IList<T> list = ((IPetraHazelcastConfig) RGraphComputer.getConfig())
          .getHazelcastClient()
          .getList(getUniqueId());

  public HazelcastListWrapper(String name){
    super(name);
  }

  private void readObject(java.io.ObjectInputStream stream)
          throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    list = ((IPetraHazelcastConfig) RGraphComputer.getConfig())
            .getHazelcastClient()
            .getList(getUniqueId());
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
  public <T1> T1[] toArray(T1[] a) {
    return list.toArray(a);
  }

  @Override
  public boolean add(T t) {
    return list.add(t);
  }

  @Override
  public boolean remove(Object o) {
    return list.remove(o);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return list.containsAll(c);
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    return list.addAll(c);
  }

  @Override
  public boolean addAll(int index, Collection<? extends T> c) {
    return list.addAll(index, c);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return list.removeAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return list.retainAll(c);
  }

  @Override
  public void replaceAll(UnaryOperator<T> operator) {
    list.replaceAll(operator);
  }

  @Override
  public void sort(Comparator<? super T> c) {
    list.sort(c);
  }

  @Override
  public void clear() {
    list.clear();
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof List){
      List l =  ((List) o);
      int length = l.size();
      for (int i=0;i<length;i++){
        if (!l.get(i).equals(list.get(i))){
          return false;
        }
      }
      return true;
    }
    return false;
    //return list.equals(o);
  }

  @Override
  public int hashCode() {
    return list.hashCode();
  }

  @Override
  public T get(int index) {
    return list.get(index);
  }

  @Override
  public T set(int index, T element) {
    return list.set(index, element);
  }

  @Override
  public void add(int index, T element) {
    list.add(index, element);
  }

  @Override
  public T remove(int index) {
    return list.remove(index);
  }

  @Override
  public int indexOf(Object o) {
    return list.indexOf(o);
  }

  @Override
  public int lastIndexOf(Object o) {
    return list.lastIndexOf(o);
  }

  @Override
  public ListIterator<T> listIterator() {
    return list.listIterator();
  }

  @Override
  public ListIterator<T> listIterator(int index) {
    return list.listIterator(index);
  }

  @Override
  public List<T> subList(int fromIndex, int toIndex) {
    return list.subList(fromIndex, toIndex);
  }

  @Override
  public Spliterator<T> spliterator() {
    return list.spliterator();
  }

  @Override
  public boolean removeIf(Predicate<? super T> filter) {
    return list.removeIf(filter);
  }

  @Override
  public Stream<T> stream() {
    return list.stream();
  }

  @Override
  public Stream<T> parallelStream() {
    return list.parallelStream();
  }

  @Override
  public void forEach(Consumer<? super T> action) {
    list.forEach(action);
  }

  @Override
  public String getName() {
    return list.getName();
  }

  @Override
  public String addItemListener(ItemListener<T> itemListener, boolean b) {
    return list.addItemListener(itemListener, b);
  }

  @Override
  public boolean removeItemListener(String s) {
    return list.removeItemListener(s);
  }

  @Override
  public String getPartitionKey() {
    return list.getPartitionKey();
  }

  @Override
  public String getServiceName() {
    return list.getServiceName();
  }

  @Override
  public void destroy() {
    list.destroy();
  }
}
