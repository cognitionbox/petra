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

import io.cognitionbox.petra.util.function.IConsumer;
import io.cognitionbox.petra.util.function.IFunction;
import io.cognitionbox.petra.util.function.IPredicate;
import io.cognitionbox.petra.util.function.IToIntFunction;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.*;
import java.util.stream.*;

public class PStream<T>  {
    public static void main(String[] args){
        System.out.println("");
        new PStream<Integer>(Stream.of(1,2,3,4,5)).filter(s->s%2==0).forEach(s->System.out.println(s));
    }

    public Stream<T> getBackingStream() {
        return stream;
    }

    private Stream<T> stream;

    public PStream(Stream<T> stream) {
        this.stream = stream;
    }

    public PStream<T> filter(IPredicate<? super T> predicate) {
        return new PStream<>(stream.filter(predicate));
    }

    public <R> PStream<R> map(IFunction<? super T, ? extends R> mapper) {
        return new PStream<>(stream.map(mapper));
    }

    public IntStream mapToInt(IToIntFunction<? super T> mapper) {
        return stream.mapToInt(mapper);
    }

    public LongStream mapToLong(ToLongFunction<? super T> mapper) {
        return stream.mapToLong(mapper);
    }

    public DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
        return stream.mapToDouble(mapper);
    }

//    public <R> PStream<R> flatMap(IFunction<? super T, ? extends Stream<? extends R>> mapper) {
//        return new PStream<R>(stream.flatMap(mapper));
//    }

    public IntStream flatMapToInt(IFunction<? super T, ? extends IntStream> mapper) {
        return stream.flatMapToInt(mapper);
    }

    public LongStream flatMapToLong(IFunction<? super T, ? extends LongStream> mapper) {
        return stream.flatMapToLong(mapper);
    }

    public DoubleStream flatMapToDouble(IFunction<? super T, ? extends DoubleStream> mapper) {
        return stream.flatMapToDouble(mapper);
    }

    public Stream<T> distinct() {
        return stream.distinct();
    }

    public Stream<T> sorted() {
        return stream.sorted();
    }

    public Stream<T> sorted(Comparator<? super T> comparator) {
        return stream.sorted(comparator);
    }

    public PStream<T> peek(IConsumer<? super T> action) {
        return new PStream<>(stream.peek(action));
    }

    public PStream<T> limit(long maxSize) {
        return new PStream<>(stream.limit(maxSize));
    }

    public PStream<T> skip(long n) {
        return new PStream<>(stream.skip(n));
    }

    public void forEach(IConsumer<? super T> action) {
        stream.forEach(action);
    }

    public void forEachOrdered(IConsumer<? super T> action) {
        stream.forEachOrdered(action);
    }

    public Object[] toArray() {
        return stream.toArray();
    }

    public <A> A[] toArray(IntFunction<A[]> generator) {
        return stream.toArray(generator);
    }

    public T reduce(T identity, BinaryOperator<T> accumulator) {
        return stream.reduce(identity, accumulator);
    }

    public Optional<T> reduce(BinaryOperator<T> accumulator) {
        return stream.reduce(accumulator);
    }

    public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
        return stream.reduce(identity, accumulator, combiner);
    }

//    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
//        return stream.collect(supplier, accumulator, combiner);
//    }
//
//    public <R, A> R collect(Collector<? super T, A, R> collector) {
//        return stream.collect(collector);
//    }

    public PList<T> toXList(){
        return stream.collect(Collectors.toCollection(()->new PList<>()));
    }
    public PSet<T> toXSet(){
        return stream.collect(Collectors.toCollection(()->new PSet<>()));
    }

    public Optional<T> min(Comparator<? super T> comparator) {
        return stream.min(comparator);
    }

    public Optional<T> max(Comparator<? super T> comparator) {
        return stream.max(comparator);
    }

    public long count() {
        return stream.count();
    }

    public boolean anyMatch(IPredicate<? super T> predicate) {
        return stream.anyMatch(predicate);
    }

    public boolean allMatch(IPredicate<? super T> predicate) {
        return stream.allMatch(predicate);
    }

    public boolean noneMatch(IPredicate<? super T> predicate) {
        return stream.noneMatch(predicate);
    }

    public Optional<T> findFirst() {
        return stream.findFirst();
    }

    public Optional<T> findAny() {
        return stream.findAny();
    }

    public static <T1> Stream.Builder<T1> builder() {
        return Stream.builder();
    }

    public static <T1> Stream<T1> empty() {
        return Stream.empty();
    }

    public static <T1> Stream<T1> of(T1 t1) {
        return Stream.of(t1);
    }

    @SafeVarargs
    public static <T1> Stream<T1> of(T1... values) {
        return Stream.of(values);
    }

    public static <T1> Stream<T1> iterate(T1 seed, UnaryOperator<T1> f) {
        return Stream.iterate(seed, f);
    }

    public static <T1> Stream<T1> generate(Supplier<T1> s) {
        return Stream.generate(s);
    }

    public static <T1> Stream<T1> concat(Stream<? extends T1> a, Stream<? extends T1> b) {
        return Stream.concat(a, b);
    }


    public Iterator<T> iterator() {
        return stream.iterator();
    }


    public Spliterator<T> spliterator() {
        return stream.spliterator();
    }

    public boolean isParallel() {
        return stream.isParallel();
    }


    public Stream<T> sequential() {
        return stream.sequential();
    }


    public Stream<T> parallel() {
        return stream.parallel();
    }


    public Stream<T> unordered() {
        return stream.unordered();
    }


    public Stream<T> onClose(Runnable closeHandler) {
        return stream.onClose(closeHandler);
    }

    public void close() {
        stream.close();
    }
}
