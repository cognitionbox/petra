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
package io.cognitionbox.petra.util;

import io.cognitionbox.petra.core.IJoin;
import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.core.impl.OperationType;
import io.cognitionbox.petra.factory.IPetraComponentsFactory;
import io.cognitionbox.petra.factory.PetraParallelComponentsFactory;
import io.cognitionbox.petra.factory.PetraSequentialComponentsFactory;
import io.cognitionbox.petra.lang.Void;
import io.cognitionbox.petra.lang.*;
import io.cognitionbox.petra.util.function.*;
import io.cognitionbox.petra.util.impl.PList;
import io.cognitionbox.petra.util.impl.PMap;
import io.cognitionbox.petra.util.impl.PSet;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Stream;

public class Petra {
    private static final Logger LOG = LoggerFactory.getLogger(Petra.class);
    public static List EMPTY = new ArrayList<>();
    public static NullType NULL = new NullType();

    public static <T> T logInfo(Object toPrint, T toPassThrough) {
        LOG.info(toPrint.toString());
        return toPassThrough;
    }

    public static <T> T println(Object toPrint, T toPassThrough) {
        System.out.println(toPrint);
        return toPassThrough;
    }

    public static <T> T optional(T value) {
        return value != null ? value : null;
    }

    public static <T> T optional(T value, IPredicate<T> predicate) {
        return predicate.test(value) ? value : null;
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

    public static <T extends IStep<?, ?>> T createStep(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new UnsupportedOperationException("cannot create step.");
        }
    }

    public static <T extends IJoin> T createJoin(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new UnsupportedOperationException("cannot create joinSome.");
        }
    }

    // can potentially do readConsume checks here on the predicate itself, maybe on just the string,
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

    public static <T> GuardRead<T> readOnly(Class<T> eventClazz, IPredicate<T> predicate) {
        return new GuardRead<>(eventClazz, predicate);
    }

    public static <T> GuardWrite<T> readWrite(Class<T> eventClazz, IPredicate<T> predicate) {
        return new GuardWrite<>(eventClazz, predicate);
    }

    public static <T> GuardConsume<T> readConsume(Class<T> eventClazz, IPredicate<T> predicate) {
        return new GuardConsume<>(eventClazz, predicate);
    }

    public static <T> GuardReturn<T> returns(Class<T> eventClazz, IPredicate<T> predicate) {
        return new GuardReturn<>(eventClazz, predicate);
    }

    public static GuardReturn<Void> returns(Class<Void> eventClazz) {
        return new GuardReturn<>(eventClazz, x -> true);
    }

    public static <I, O> PEdge<I, O> anonymous(Guard<? super I> p,
                                               IFunction<I, O> function,
                                               Guard<O>... qs) {
        GuardXOR<O> pTypeXOR = new GuardXOR<>(OperationType.RETURN);
        for (Guard q : qs) {
            pTypeXOR.addChoice(q);
        }
        return new PEdge(p, function, pTypeXOR);
    }

    public static <A, R> PJoin<A, R> anonymousJ1(Guard<? super A> a, IFunction<List<A>, R> function, Guard<? super R> r) {
        return new PJoin<>(a, function, r);
    }

    public static <A, B, R> PJoin2<A, B, R> anonymousJ2(Guard<? super A> a, Guard<? super B> b, IBiFunction<List<A>, List<B>, R> function, Guard<? super R> r) {
        return new PJoin2(a, b, function, r);
    }

    public static <A, B, C, R> PJoin3<A, B, C, R> anonymousJ3(Guard<? super A> a, Guard<? super B> b, Guard<? super C> c, ITriFunction<List<A>, List<B>, List<C>, R> function, Guard<? super R> r) {
        return new PJoin3(a, b, c, function, r);
    }

//    public static <T> Guard<T> readConsume(Class<T> eventClazz) {
//        return readConsume(eventClazz, v -> v!=null);
//    }

    public static <A, B, C, T extends Triplet<List<A>, List<B>, List<C>>> Guard<T> tripleList(
            Class<T> clazz,
            IPredicate<A> a,
            IPredicate<B> b,
            IPredicate<C> c) {
        return readConsume(clazz, v -> v.getValue0().stream().allMatch(a) &&
                v.getValue1().stream().allMatch(b) &&
                v.getValue2().stream().allMatch(c));
    }

    public static <A, T extends List<A>> Guard<T> list(
            Class<T> clazz,
            IPredicate<A> a) {
        return readConsume(clazz, v -> v.stream().allMatch(a));
    }

    public static <A, B, T extends Pair<List<A>, List<B>>> Guard<T> listPair(
            Class<T> clazz,
            IPredicate<A> a,
            IPredicate<B> b) {
        return readConsume(clazz, v -> v.getValue0().stream().allMatch(a) &&
                v.getValue1().stream().allMatch(b));
    }

    public static <A, B, C, T extends Triplet<List<A>, List<B>, List<C>>> Guard<T> listTriple(
            Class<T> clazz,
            IPredicate<A> a,
            IPredicate<B> b,
            IPredicate<C> c) {
        return readConsume(clazz, v -> v.getValue0().stream().allMatch(a) &&
                v.getValue1().stream().allMatch(b) &&
                v.getValue2().stream().allMatch(c));
    }

    public static <T> Guard<T> False(Class<T> eventClazz) {
        return readConsume(eventClazz, v -> false);
    }

    public static <T> Guard<T> True(Class<T> eventClazz) {
        return readConsume(eventClazz, v -> true);
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

    public static <T> List<T> list() {
        return new PList<>();
        // (List<T>) create(null,()->getFactory().createList(UUID.randomUUID().toString()));
    }

    public static Set set() {
        return new PSet();
        //(Set) create(null,()->getFactory().createSet(UUID.randomUUID().toString()));
    }

    public static Map map() {
        return new PMap();
        //(Map) create(null,()->getFactory().createMap(UUID.randomUUID().toString()));
    }

    public static <T> List<T> list(ISupplier<List<T>> supplier) {
        return (List<T>) create(supplier, () -> getFactory().createList(UUID.randomUUID().toString()));
    }

    public static Queue queue(ISupplier<Queue> supplier) {
        return (Queue) create(supplier, () -> getFactory().createQueue(UUID.randomUUID().toString()));
    }

    public static Set set(ISupplier<Set> supplier) {
        return (Set) create(supplier, () -> getFactory().createSet(UUID.randomUUID().toString()));
    }

    public static Map map(ISupplier<Map> supplier) {
        return (Map) create(supplier, () -> getFactory().createMap(UUID.randomUUID().toString()));
    }

//    public static <T> Guard<T> readConsume(IPredicate predicate) {
//        return new Guard<>(Object.class, predicate);
//    }

    // link ptype useages in the same graph

    // use weakerThan, strongerThan relationships instead of connectives below
    // its more light weight for setting relationships than can be used for
    // reachability checking

//    public static <T,S extends T>  PTypeAND<T> and(Guard<T>... types) {
//        return new PTypeAND<T>(x -> {
//            return Arrays.asList(types).stream().allMatch(p->p.test(x));
//        });
//    }

//    public static <T>  PTypeNOT<T> not(Guard<T> Guard) {
//        return new PTypeNOT<T>(x-> Guard.negate().test((T) x));
//    }

//    public static <T,C extends Ref<T>> C someRef(T readConsume, Class<C> clazz) {
//        return (C) getFactory().createRef(readConsume, UUID.randomUUID().toString());
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

    public static class NullType implements Serializable {
        private NullType() {
        }
    }

    //    @Extract
    public static class T2<A, B> extends Tuple {
        private A a;
        private B b;

        public T2(A a, B b) {
            super(a, b);
            this.a = a;
            this.b = b;
        }

        public A get1() {
            return (A) get(0);
        }

        public B get2() {
            return (B) get(1);
        }
    }

    public static class T3<A, B, C> extends T2<A, B> {
        private C c;

        public T3(A a, B b, C c) {
            super(a, b);
            this.c = c;
        }

        public A get3() {
            return (A) get(0);
        }
    }

    public static class Tuple implements Iterable<Object>, Serializable {

        private List<Object> values;

        public Tuple(Object... values) {
            this.values = Arrays.asList(values);
        }

        public Object get(int index) {
            if (index >= this.values.size()) {
                return null;
            }
            return this.values.get(index);
        }

        //final public <T> T getDotOutput(Class<T> clazz, int index) {
        //    return (T) getDotOutput(index);
        //}

        @Override
        public Iterator<Object> iterator() {
            return values.iterator();
        }
    }
}
