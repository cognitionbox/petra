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
package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.core.IJoin;
import io.cognitionbox.petra.util.function.ITriFunction;

import java.util.List;

public abstract class AbstractJoin3<A, B, C, R> extends AbstractJoin<R> implements IJoin {

    private Guard<? super A> a;
    private Guard<? super B> b;
    private Guard<? super C> c;
    private long millisBeforeRetry = 100;

    AbstractJoin3(Guard<? super A> a, Guard<? super B> b, Guard<? super C> c,
                  ITriFunction<List<A>, List<B>,List<C>,R> function,
                  Guard<? super R> r) {
        super(r);
        this.a = a;
        this.b = b;
        this.c = c;
        this.function = function;
    }

    protected AbstractJoin3() {
    }

    public Guard<? super A> a() {
        return a;
    }

    public Guard<? super B> b() {
        return b;
    }

    public Guard<? super C> c() {
        return c;
    }

    protected void preA(GuardInput<? super A> a) {
        addInputType(a);
        this.a = a;
    }

    protected void preB(GuardInput<? super B> b) {
        addInputType(b);
        this.b = b;
    }

    protected void preC(GuardInput<? super C> c) {
        addInputType(c);
        this.c = c;
    }

    public long getMillisBeforeRetry() {
        return millisBeforeRetry;
    }

    protected void setMillisBeforeRetry(long millisBeforeRetry) {
        this.millisBeforeRetry = millisBeforeRetry;
    }

   // abstract ITriFunction func();

    private ITriFunction<List<A>, List<B>,List<C>,R> function;
    protected void func(ITriFunction<List<A>, List<B>, List<C>, R> function) {
        this.function = function;
    }

    public ITriFunction<List<A>, List<B>,List<C>, R> func(){
        return function;
    }

}
