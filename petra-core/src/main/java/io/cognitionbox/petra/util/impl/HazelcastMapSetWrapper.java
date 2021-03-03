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

import com.hazelcast.core.IMap;
import io.cognitionbox.petra.core.impl.Identifyable;
import io.cognitionbox.petra.lang.RGraphComputer;
import io.cognitionbox.petra.lang.config.IPetraHazelcastConfig;
import io.cognitionbox.petra.util.Petra;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class HazelcastMapSetWrapper<T> extends Identifyable implements Set<T>, Serializable {

    public Set<T> getSet() {
        return this;
    }

    public IMap<T, Boolean> getBackingJetMap() {
        return jetMap;
    }

    private transient IMap<T, Boolean> jetMap = ((IPetraHazelcastConfig) RGraphComputer.getConfig())
            .getHazelcastClient()
            .getMap(getUniqueId());

    public HazelcastMapSetWrapper(String name) {
        super(name);
    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        jetMap = ((IPetraHazelcastConfig) RGraphComputer.getConfig())
                .getHazelcastClient()
                .getMap(getUniqueId());
    }

//  private static class NonEmptySetFromMap<E> extends AbstractSet<E>
//          implements Set<E>, Serializable
//  {
//    private final Map<E, Boolean> m;  // The backing map
//    private transient Set<E> s;       // Its keySet
//
//    NonEmptySetFromMap(Map<E, Boolean> map) {
//      m = map;
//      s = map.keySet();
//    }
//
//    public void clear()               {        m.clear(); }
//    public int size()                 { return m.size(); }
//    public boolean isEmpty()          { return m.isEmpty(); }
//    public boolean contains(Object o) { return m.containsKey(o); }
//    public boolean removeState(Object o)   { return m.removeState(o) != null; }
//    public boolean add(E e) { return m.put(e, Boolean.TRUE) == null; }
//    public Iterator<E> iterator()     { return s.iterator(); }
//    public Object[] toArray()         { return s.toArray(); }
//    public <T> T[] toArray(T[] a)     { return s.toArray(a); }
//    public String toString()          { return s.toString(); }
//    public int hashCode()             { return s.hashCode(); }
//    public boolean equals(Object o)   { return o == this || s.equals(o); }
//    public boolean containsAll(Collection<?> c) {return s.containsAll(c);}
//    public boolean removeAll(Collection<?> c)   {return s.removeAll(c);}
//    public boolean retainAll(Collection<?> c)   {return s.retainAll(c);}
//    // addAll is the only inherited implementation
//
//    // Override default methods in Collection
//    @Override
//    public void forEach(Consumer<? super E> action) {
//      s.forEach(action);
//    }
//    @Override
//    public boolean removeIf(Predicate<? super E> filter) {
//      return s.removeIf(filter);
//    }
//
//    @Override
//    public Spliterator<E> spliterator() {return s.spliterator();}
//    @Override
//    public Stream<E> stream()           {return s.stream();}
//    @Override
//    public Stream<E> parallelStream()   {return s.parallelStream();}
//
//    private static final long serialVersionUID = 2454657854757543876L;
//
//    private void readObject(java.io.ObjectInputStream stream)
//            throws IOException, ClassNotFoundException
//    {
//      stream.defaultReadObject();
//      s = m.keySet();
//    }
//  }

    @Override
    public int size() {
        return jetMap.size();
    }

    @Override
    public boolean isEmpty() {
        return jetMap.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return jetMap.containsKey(o);
    }

    @Override
    public Iterator<T> iterator() {
        return jetMap.keySet().iterator();
    }

    @Override
    public Object[] toArray() {
        return jetMap.keySet().toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return jetMap.keySet().toArray(a);
    }

    @Override
    public boolean add(T t) {
        jetMap.put(t, true);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (jetMap.remove(o) != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return jetMap.keySet().containsAll(c);
    }

    private Stream<T> collectionStream(Collection collection) {
        if (collection instanceof PList) {
            return Petra.listStream((PList) collection);
        } else if (collection instanceof PSet) {
            return Petra.setStream((PSet) collection);
        } else if (collection instanceof PMap) {
            return Petra.mapStream((PMap) collection);
        } else {
            return collection.stream();
        }
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        collectionStream(c).forEach(e -> add(e));
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        collectionStream(c).filter(e -> !contains(e)).forEach(e -> remove(e));
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        collectionStream(c).forEach(e -> remove(e));
        return true;
    }

    @Override
    public void clear() {
        jetMap.clear();
    }

    @Override
    public boolean equals(Object o) {
        return jetMap.keySet().equals(o);
    }

    @Override
    public int hashCode() {
        return jetMap.keySet().hashCode();
    }

    @Override
    public Spliterator<T> spliterator() {
        return jetMap.keySet().spliterator();
    }

    // Needs reviewing
    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        Petra.mapStream(jetMap).map(e -> e.getKey()).filter(filter).forEach(e -> remove(e));
        return true;
    }

    @Override
    public Stream<T> stream() {
        return jetMap.keySet().stream();
    }

    // more convenient for cross platform streams
    @Override
    public Stream<T> parallelStream() {
        return jetMap.keySet().parallelStream();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        jetMap.keySet().forEach(action);
    }

}
