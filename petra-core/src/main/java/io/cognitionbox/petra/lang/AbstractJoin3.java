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
