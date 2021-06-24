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
package io.cognitionbox.petra.util;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.core.impl.ObjectCopyerViaSerialization;
import io.cognitionbox.petra.factory.IPetraComponentsFactory;
import io.cognitionbox.petra.lang.Guard;
import io.cognitionbox.petra.lang.GuardReturn;
import io.cognitionbox.petra.lang.GuardWrite;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.RGraphComputer;
import io.cognitionbox.petra.lang.Ref;
import io.cognitionbox.petra.lang.Void;
import io.cognitionbox.petra.util.function.IConsumer;
import io.cognitionbox.petra.util.function.IFunction;
import io.cognitionbox.petra.util.function.IPredicate;
import io.cognitionbox.petra.util.function.ISupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
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
            throw new UnsupportedOperationException("cannot create step.", e);
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

    public static ExecMode seq() {
        return ExecMode.SEQ;
    }

    public static ExecMode par() {
        return ExecMode.PAR;
    }

    public static <T> boolean forAll(Class<T> eventClazz, Collection<? extends T> collection, IPredicate<T> predicate) {
        return collection.stream().allMatch(predicate);
    }

    public static <T> boolean forall(Collection<? extends T> collection, IPredicate<T> predicate) {
        return collection.stream().allMatch(predicate);
    }

    public static <T> boolean thereExists(Class<T> eventClazz, Collection<? extends T> collection, IPredicate<T> predicate) {
        return collection.stream().anyMatch(predicate);
    }

    public static <T> GuardWrite<T> rw(Class<T> eventClazz, IPredicate<T> predicate) {
        return new GuardWrite<>(eventClazz, predicate);
    }

    public static <T> GuardReturn<T> rt(Class<T> eventClazz, IPredicate<T> predicate) {
        return new GuardReturn<>(eventClazz, predicate);
    }

    public static GuardReturn<Void> rt(Class<Void> eventClazz) {
        return new GuardReturn<>(eventClazz, x -> true);
    }

    public static <X> PEdge<X> anonymous(Guard<X> p,
                                         IConsumer<X> function,
                                         Guard<X> q) {
        return new PEdge(p, function, q);
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

    public static <T> Ref<T> ref(T value, String id) {
        return (Ref<T>) getFactory().createRef(value, id);
    }

    public static <T> Ref<T> ref(T value) {
        return ref(value, UUID.randomUUID().toString());
    }

    public static <T> Ref<T> ref() {
        return ref(null);
    }

    private static ObjectCopyerViaSerialization copyer = new ObjectCopyerViaSerialization();

    public static <T extends Serializable> T copy(T object) {
        try {
            return copyer.copy(object);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <C extends Collection<R>, T extends Serializable, R> C mapParallel(Collection<T> collection, IFunction<T, R> mapper, ISupplier<C> supplier) {
        IFunction<T, R> mapperWithCopy = t -> {
            if (t != null) {
                return mapper.apply(copy(t));
            } else {
                return null;
            }
        };
        return collection.parallelStream().map(mapperWithCopy).collect(Collectors.toCollection(supplier));
    }

    public static <C extends Collection<R>, T, R> C mapSequential(Collection<T> collection, IFunction<T, R> mapper, ISupplier<C> supplier) {
        return collection.stream().map(mapper).collect(Collectors.toCollection(supplier));
    }

    public static <T extends Serializable> Optional<T> maxParallel(Collection<T> collection, Comparator<T> comparator) {
        Comparator<T> comparatorWithCopy = (t1, t2) -> {
            return comparator.compare(copy(t1), copy(t2));
        };
        return collection.parallelStream().max(comparatorWithCopy);
    }

    public static <T> Optional<T> maxSequential(Collection<T> collection, Comparator<T> comparator) {
        return collection.stream().max(comparator);
    }

    public static <C extends Collection<T>, T extends Serializable> C filterParallel(Collection<T> collection, IPredicate<T> predicate, ISupplier<C> supplier) {
        IPredicate<T> predicateWithCopy = t -> {
            return t != null && predicate.test(copy(t));
        };
        return collection.parallelStream().filter(predicateWithCopy).collect(Collectors.toCollection(supplier));
    }

    public static <C extends Collection<T>, T extends Serializable> C filterSequential(Collection<T> collection, IPredicate<T> predicate, ISupplier<C> supplier) {
        return collection.stream().filter(predicate).collect(Collectors.toCollection(supplier));
    }

    public static boolean greatThan(int a, int b) {
        return a>b;
    }

    public static boolean lessThan(int a, int b) {
        return a<b;
    }

    public static boolean greatThan(double a, double b) {
        return a>b;
    }

    public static boolean lessThan(double a, double b) {
        return a<b;
    }

    public static boolean isBetweenExclusive(double a, double b, double c) {
        return lessThan(a,b) && lessThan(b,c);
    }

    public static boolean equal(boolean a, boolean b) {
        return a==b;
    }

}
