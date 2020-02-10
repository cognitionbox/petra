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
import io.cognitionbox.petra.util.IPSet;
import io.cognitionbox.petra.util.PCollection;
import io.cognitionbox.petra.util.Petra;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class PSet<T> extends Identifyable implements IPSet<T>, Serializable , PCollection<T> {

  public Set<T> getSet() {
    return set;
  }

  private Set<T> set = Petra.getFactory().createSet(getUniqueId());

  public PSet() {}

  public PSet(Collection<T> collection) {
    set.addAll(collection);
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
  public <T1> T1[] toArray( T1[] a) {
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
  public boolean containsAll( Collection<?> c) {
    return set.containsAll(c);
  }

  @Override
  public boolean addAll( Collection<? extends T> c) {
    return set.addAll(c);
  }

  @Override
  public boolean retainAll( Collection<?> c) {
    return set.retainAll(c);
  }

  @Override
  public boolean removeAll( Collection<?> c) {
    return set.removeAll(c);
  }

  @Override
  public void clear() {
    set.clear();
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Set){
      Set toTest = (Set)o;
      // will equals be affected by race conditions?
      // they should not, equalities happen at pre/post condition checks and if within a computation
      // they should happen within queue actors operations or on on the mutually exclusive objects,
      // this is enforced by a combination of the XOR check and by developer convention
      return set.containsAll(toTest);
    }
    return false;
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
  public void forEach(Consumer<? super T> action) {
    set.forEach(action);
  }

  @Override
  public String toString(){
    return set.toString();
  }
}
