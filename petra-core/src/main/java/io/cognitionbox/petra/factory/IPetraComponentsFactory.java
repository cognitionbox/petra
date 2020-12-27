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
package io.cognitionbox.petra.factory;

import io.cognitionbox.petra.core.engine.petri.Place;
import io.cognitionbox.petra.lang.RO;
import io.cognitionbox.petra.lang.RW;
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

  <T> RW<T> createRW(T value, String name);

  <T> RO<T> createRO(T value, String name);

  <T> Stream<T> createStreamFromList(List<T> list);

  <K, V> Stream<Entry<K, V>> createStreamFromMap(Map<K, V> map);

  <T,V> Stream<T> createStreamFromSet(Set<T> set);

  <T> Stream<T> createStreamFromIterable(Iterable<T> iterable);
}