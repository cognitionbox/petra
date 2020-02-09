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

import io.cognitionbox.petra.lang.annotations.Pure;
import io.cognitionbox.petra.core.IJoin2;
import io.cognitionbox.petra.util.function.IBiFunction;

import java.util.List;

@Pure
public class PJoin2<A,B,R> extends AbstractJoin2<A,B,R> implements IJoin2<A, B, R> {


    private long millisBeforeRetry = 100;
    public PJoin2(){}
    public PJoin2(Guard<? super A> a, Guard<? super B> b, IBiFunction<List<A>, List<B>, R> function, Guard<? super R> r) {
        super(a, b, function, r);
    }

    public long getMillisBeforeRetry() {
        return millisBeforeRetry;
    }

    protected void setMillisBeforeRetry(long millisBeforeRetry) {
        this.millisBeforeRetry = millisBeforeRetry;
    }

//    protected void preA(Class<? super A> a, IPredicate<? super A> predicate) {
//        this.a = new Guard(a,predicate);
//    }
//    protected void preB(Class<? super B> b, IPredicate<? super B> predicate) {
//        this.b = new Guard(b,predicate);
//    }

//    protected void post(returns(Class<? super R> r, IPredicate<? super R> predicate) {
//        this.r = new Guard(r,predicate);
//    }

}
