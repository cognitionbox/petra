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
package petra.lang.impls;



import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ConcurrentHashSet<T> implements Set<T>, Serializable {

  private Set<T> setFromMap = Collections.newSetFromMap(new ConcurrentHashMap<>());

  @Override
  public int size() {
    return setFromMap.size();
  }

  @Override
  public boolean isEmpty() {
    return setFromMap.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return setFromMap.contains(o);
  }

  @Override
  public Iterator<T> iterator() {
    return setFromMap.iterator();
  }

  @Override
  public Object[] toArray() {
    return setFromMap.toArray();
  }

  @Override
  public <T1> T1[] toArray( T1[] a) {
    return setFromMap.toArray(a);
  }

  @Override
  public boolean add(T t) {
    return setFromMap.add(t);
  }

  @Override
  public boolean remove(Object o) {
    return setFromMap.remove(o);
  }

  @Override
  public boolean containsAll( Collection<?> c) {
    return setFromMap.containsAll(c);
  }

  @Override
  public boolean addAll( Collection<? extends T> c) {
    return setFromMap.addAll(c);
  }

  @Override
  public boolean retainAll( Collection<?> c) {
    return setFromMap.retainAll(c);
  }

  @Override
  public boolean removeAll( Collection<?> c) {
    return setFromMap.removeAll(c);
  }

  @Override
  public void clear() {
    setFromMap.clear();
  }

  @Override
  public boolean equals(Object o) {
    return setFromMap.equals(o);
  }

  @Override
  public int hashCode() {
    return setFromMap.hashCode();
  }

  @Override
  public Spliterator<T> spliterator() {
    return setFromMap.spliterator();
  }

  @Override
  public boolean removeIf(Predicate<? super T> filter) {
    return setFromMap.removeIf(filter);
  }

  @Override
  public Stream<T> stream() {
    return setFromMap.stream();
  }

  @Override
  public Stream<T> parallelStream() {
    return setFromMap.parallelStream();
  }

  @Override
  public void forEach(Consumer<? super T> action) {
    setFromMap.forEach(action);
  }
}
