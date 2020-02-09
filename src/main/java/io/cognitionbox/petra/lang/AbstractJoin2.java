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
import io.cognitionbox.petra.util.function.IBiFunction;

import java.util.List;

public abstract class AbstractJoin2<A,B,R> extends AbstractJoin<R> implements IJoin {

    private Guard<? super B> b;

    AbstractJoin2(Guard<? super A> a, Guard<? super B> b,
                  IBiFunction<List<A>, List<B>,R> function,
                  Guard<? super R> r) {
        super(r);
        this.a = a;
        this.b = b;
        this.function = function;
    }

    protected AbstractJoin2() {
    }

    public Guard<? super B> b() {
        return b;
    }

    private Guard<? super A> a;

    public Guard<? super A> a() {
        return a;
    }

    protected void preA(GuardInput<? super A> a) {
        addInputType(a);
        this.a = a;
    }

    protected void preB(GuardInput<? super B> b) {
        addInputType(b);
        this.b = b;
    }

    IBiFunction<List<A>, List<B>,R> function;
    public IBiFunction<List<A>, List<B>,R> func(){
        return function;
    }
    public void func(IBiFunction<List<A>, List<B>, R> function) {
        this.function = function;
    }

}
