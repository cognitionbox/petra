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

import io.cognitionbox.petra.config.IPetraHazelcastConfig;
import io.cognitionbox.petra.core.IRingbuffer;
import io.cognitionbox.petra.core.engine.petri.Place;
import io.cognitionbox.petra.core.engine.petri.impl.HazelcastPlace;
import io.cognitionbox.petra.lang.RGraphComputer;
import io.cognitionbox.petra.lang.RO;
import io.cognitionbox.petra.lang.RW;
import io.cognitionbox.petra.util.impl.*;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class PetraHazelcastComponentsFactory implements IPetraComponentsFactory {

    public static ExecutorService blockingExecutorService(int queueSize, int threadCount) {
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(queueSize);
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(threadCount, threadCount, 20, TimeUnit.SECONDS, queue);
        return tpe;
    }

    @Override
    public IRingbuffer createRingbuffer(String s) {
        return new HazelcastRingBufferWrapper(s);
    }

    @Override
    public ExecutorService createExecutorService(String id) {
//        return ((IPetraHazelcastConfig) RGraphComputer.getConfig())
//                .getHazelcastClient().getExecutorService(id);
        return blockingExecutorService(1,Runtime.getRuntime().availableProcessors()*2);
    }

    @Override
    public Lock createLock(String id) {
        return ((IPetraHazelcastConfig) RGraphComputer.getConfig())
                .getHazelcastClient().getLock(id);
    }

    @Override
    public Set createSet(String id) {
        return new HazelcastMapSetWrapper(id);
    }

    @Override
    public Queue createQueue(String id) {
        return ((IPetraHazelcastConfig) RGraphComputer.getConfig())
                .getHazelcastClient().getQueue(id);
    }

    @Override
    public List createList(String id) {
        return new HazelcastListWrapper(id);
    }

    @Override
    public Stream createStreamFromList(List list) {
        return list.parallelStream();
    }

    @Override
    public Stream createStreamFromMap(Map map) {
        return map.entrySet().parallelStream();
    }

    // Not useful at the moment as newSetFromMap has issues, using imdg set instead, does not stream
    @Override
    public <T, V> Stream<T> createStreamFromSet(Set<T> set) {
        return set.parallelStream();
    }

    @Override
    public <T> Stream<T> createStreamFromIterable(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    @Override
    public Map createMap(String id) {
        return new HazelcastMapWrapper(id);
    }

    @Override
    public Place createPlace(String s) {
        return new HazelcastPlace(s);
    }

    @Override
    public <T> RW<T> createRW(T value, String name) {
        return new HazelcastAtomicReferenceWrapperRW(value, name);
    }

    @Override
    public <T> RO<T> createRO(T value, String name) {
        return new HazelcastAtomicReferenceWrapperRO(value, name);
    }

}
