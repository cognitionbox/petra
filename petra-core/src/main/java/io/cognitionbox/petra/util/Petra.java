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
package io.cognitionbox.petra.util;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.factory.IPetraComponentsFactory;
import io.cognitionbox.petra.lang.*;
import io.cognitionbox.petra.util.function.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Stream;

public class Petra {
    private static final Logger LOG = LoggerFactory.getLogger(Petra.class);
    public static List EMPTY = new ArrayList<>();

    public static <T> T logInfo(Object toPrint, T toPassThrough) {
        LOG.info(toPrint.toString());
        return toPassThrough;
    }

    public static <T> T println(Object toPrint, T toPassThrough) {
        System.out.println(toPrint);
        return toPassThrough;
    }

    public static Class<? extends Throwable>[] throwsRandom(Class<? extends Throwable>... clazzes) {
        return clazzes;
    }

    public static void throwRandomException(List<Class<? extends Exception>> randomlyThrows) throws Exception {
        Exception t = null;
        try {
            int idx = new SecureRandom().nextInt(randomlyThrows.size() + 1);
            if (idx == 0) {
                // do not throw allowing computation to happen
                return;
            }
            // pick a random exception to throw
            t = randomlyThrows.get(idx).newInstance();
        } catch (Exception e) {
        }
        if (t != null) {
            throw t;
        }
    }

    public static <T extends IStep<?>> T createStep(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new UnsupportedOperationException("cannot create step.");
        }
    }

    // can potentially do rw checks here on the predicate itself, maybe on just the string,
    // or on the actually logic using a symbolic reasoner
    private static Map<Class<? extends Guard>, Guard> singletonTypeCache = new HashMap<>();

    public static Guard<?> state(Class<? extends Guard<?>> clazz) {
        try {
            if (!singletonTypeCache.containsKey(clazz)) {
                singletonTypeCache.put(clazz, clazz.newInstance());
            }
            return singletonTypeCache.get(clazz);
        } catch (Exception e) {
            throw new UnsupportedOperationException("cannot create step.");
        }
    }

    public static ExecMode seq(){
        return ExecMode.SEQ;
    }

    public static ExecMode par(){
        return ExecMode.PAR;
    }

    public static <T> boolean forAll(Class<T> eventClazz, Collection<? extends T> collection, IPredicate<T> predicate) {
        return collection.stream().allMatch(predicate);
    }

    public static <T> boolean thereExists(Class<T> eventClazz, Collection<? extends T> collection, IPredicate<T> predicate) {
        return collection.stream().anyMatch(predicate);
    }

    public static IPetraComponentsFactory getFactory() {
        switch (RGraphComputer.getConfig().getMode()) {
            case SEQ: {
                return RGraphComputer.getConfig().getSequentialModeFactory();
            }
            case PAR: {
                return RGraphComputer.getConfig().getParallelModeFactory();
            }
            case DIS: {
                return RGraphComputer.getConfig().getDistributedModeFactory();
            }
        }
        return null;
    }

    private static Object create(
            ISupplier explicit,
            ISupplier implicit) {
        if (explicit != null) {
            return explicit.get();
        } else {
            if (implicit == null) {
                throw new IllegalArgumentException("implicit must be supplied, if no explicit");
            }
            return implicit.get();
        }
    }

    public static <T> Stream<T> iterableStream(Iterable<T> iterable) {
        return getFactory().createStreamFromIterable(iterable);
    }

    public static <T> Stream<T> listStream(List<T> list) {
        return getFactory().createStreamFromList(list);
    }

    public static <K, V> Stream<Entry<K, V>> mapStream(Map<K, V> map) {
        return getFactory().createStreamFromMap(map);
    }

    public static <T> Stream<T> setStream(Set<T> set) {
        return getFactory().createStreamFromSet(set);
    }

//    public static <T,C extends Ref<T>> C someRef(T rw, Class<C> clazz) {
//        return (C) getFactory().createRef(rw, UUID.randomUUID().toString());
//    }

    public static List<V<?>> getVariables() {
        return variables;
    }

    private static List<V<?>> variables = new ArrayList<>();

    public static <T> RW<T> rw(T value, String id) {
        // use current thread get name to get the step name, create special thread type for this and executor
        // service which uses this thread type.
        RW<T> rw = getFactory().createRW(value, Thread.currentThread().getName(),id);
        variables.add(rw);
        return (RW<T>)rw;
    }

    public static <T> RW<T> rw(T value) {
        return rw(value, UUID.randomUUID().toString());
    }

    public static <T> RW<T> rw() {
        return rw(null);
    }


    public static <T> RO<T> ro(T value, String id) {
        // use current thread get name to get the step name, create special thread type for this and executor
        // service which uses this thread type.
        RO<T> ro = getFactory().createRO(value, Thread.currentThread().getName(),id);
        variables.add(ro);
        return (RO<T>)ro;
    }

    public static <T> RO<T> ro(T value) {
        return ro(value, UUID.randomUUID().toString());
    }

    public static <T> RO<T> ro() {
        return ro(null);
    }

}
