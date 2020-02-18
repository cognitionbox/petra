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

import io.cognitionbox.petra.core.impl.Identifyable;
import io.cognitionbox.petra.util.IPList;
import io.cognitionbox.petra.util.PCollection;
import io.cognitionbox.petra.util.Petra;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class PList<T> extends Identifyable implements IPList<T>, PCollection {

  private List<T> backingList = Petra.getFactory().createList(getUniqueId());

  public PList(){}
  public PList(String id){super(id);}

  @Override
  public String toString() {
    return this.getClass().getSimpleName()+"{" +
            "backingList=" + backingList +
            '}';
  }

  public PList(Collection<T> coll){
    backingList.addAll(coll);
  }

  public List<T> getBackingList() {
    return backingList;
  }

  @Override
  public int size() {
    return backingList.size();
  }

  @Override
  public boolean isEmpty() {
    return backingList.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return backingList.contains(o);
  }

  @Override
  public Iterator<T> iterator() {
    return backingList.iterator();
  }

  @Override
  public Object[] toArray() {
    return backingList.toArray();
  }

  @Override
  public <T1> T1[] toArray( T1[] a) {
    return backingList.toArray(a);
  }

  @Override
  public boolean add(T t) {
    return backingList.add(t);
  }

  @Override
  public boolean remove(Object o) {
    return backingList.remove(o);
  }

  @Override
  public boolean containsAll( Collection<?> c) {
    return backingList.containsAll(c);
  }

  @Override
  public boolean addAll( Collection<? extends T> c) {
    return backingList.addAll(c);
  }

  @Override
  public boolean addAll(int index,
       Collection<? extends T> c) {
    return backingList.addAll(index, c);
  }

  @Override
  public boolean removeAll( Collection<?> c) {
    return backingList.removeAll(c);
  }

  @Override
  public boolean retainAll( Collection<?> c) {
    return backingList.retainAll(c);
  }

  @Override
  public void replaceAll(UnaryOperator<T> operator) {
    backingList.replaceAll(operator);
  }

  @Override
  public void sort(Comparator<? super T> c) {
    backingList.sort(c);
  }

  @Override
  public void clear() {
    backingList.clear();
  }

  @Override
  public boolean equals(Object o) {
    return backingList.equals(o);
  }

  @Override
  public int hashCode() {
    return backingList.hashCode();
  }

  @Override
  public T get(int index) {
    return backingList.get(index);
  }

  @Override
  public T set(int index, T element) {
    return backingList.set(index, element);
  }

  @Override
  public void add(int index, T element) {
    backingList.add(index, element);
  }

  @Override
  public T remove(int index) {
    return backingList.remove(index);
  }

  @Override
  public int indexOf(Object o) {
    return backingList.indexOf(o);
  }

  @Override
  public int lastIndexOf(Object o) {
    return backingList.lastIndexOf(o);
  }

  @Override
  public ListIterator<T> listIterator() {
    return backingList.listIterator();
  }

  @Override
  public ListIterator<T> listIterator(int index) {
    return backingList.listIterator(index);
  }

  @Override
  public List<T> subList(int fromIndex, int toIndex) {
    return backingList.subList(fromIndex, toIndex);
  }

  @Override
  public Spliterator<T> spliterator() {
    return backingList.spliterator();
  }

  @Override
  public boolean removeIf(Predicate<? super T> filter) {
    return backingList.removeIf(filter);
  }

  @Override
  public void forEach(Consumer<? super T> action) {
    backingList.forEach(action);
  }

}
