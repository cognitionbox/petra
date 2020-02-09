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
import io.cognitionbox.petra.util.function.IFunction;

import java.util.List;

public abstract class AbstractJoin1<A,R> extends AbstractJoin<R> implements IJoin {

    Guard<? super A> a;

    AbstractJoin1(Guard<? super A> a, IFunction<List<A>, R> function, Guard<? super R> r) {
        super(r);
        this.a = a;
        this.function = function;
    }

    protected AbstractJoin1() {}

    public Guard<? super A> a() {
        return a;
    }

    protected void pre(GuardInput<? super A> a) {
        addInputType(a);
        this.a = a;
    }

    private long millisBeforeRetry = 100;
    public long getMillisBeforeRetry() {
        return millisBeforeRetry;
    }

    protected void setMillisBeforeRetry(long millisBeforeRetry) {
        this.millisBeforeRetry = millisBeforeRetry;
    }

//    abstract IFunction func();

    public IFunction<List<A>, R> func() {
        return function;
    }

    IFunction<List<A>, R> function;

    public void func(IFunction<List<A>, R> function) {
        this.function = function;
    }
}
