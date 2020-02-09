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
package io.cognitionbox.petra.factory;

import io.cognitionbox.petra.core.engine.petri.Place;
import io.cognitionbox.petra.lang.Ref;
import io.cognitionbox.petra.core.IRingbuffer;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.stream.Stream;

public interface IPetraComponentsFactory extends Serializable {

  IRingbuffer createRingbuffer(String name);

  ExecutorService createExecutorService(String name);

  Lock createLock(String name);

  Set createSet(String name);

  Queue createQueue(String name);

  List createList(String name);

  Map createMap(String s);

  Place createPlace(String name);

  <T> Ref<T> createRef(T value, String name);

  <T> Stream<T> createStreamFromList(List<T> list);

  <K, V> Stream<Entry<K, V>> createStreamFromMap(Map<K, V> map);

  <T,V> Stream<T> createStreamFromSet(Set<T> set);

  <T> Stream<T> createStreamFromIterable(Iterable<T> iterable);
}