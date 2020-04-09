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
import io.cognitionbox.petra.util.function.ITriConsumer;
import io.cognitionbox.petra.util.function.ITriFunction;

import java.util.List;

public abstract class AbstractEffectJoin3<A, B, C> extends AbstractJoin3<A,B,C> implements IJoin {

    private Guard<? super A> a;
    private Guard<? super B> b;
    private Guard<? super C> c;
    private long millisBeforeRetry = 100;

    Guard<? super A> postA;
    Guard<? super B> postB;
    Guard<? super C> postC;

    AbstractEffectJoin3(Guard<? super A> a, Guard<? super B> b, Guard<? super C> c,
                        ITriConsumer<List<A>, List<B>,List<C>> function) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.function = function;
    }

    protected AbstractEffectJoin3() {
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

    protected void postA(GuardReturn<? super A> a) {
        this.postA = a;
    }

    protected void postB(GuardReturn<? super B> b) {
        this.postB = b;
    }

    protected void postC(GuardReturn<? super C> c) {
        this.postC = c;
    }

    public long getMillisBeforeRetry() {
        return millisBeforeRetry;
    }

    protected void setMillisBeforeRetry(long millisBeforeRetry) {
        this.millisBeforeRetry = millisBeforeRetry;
    }

   // abstract ITriFunction func();

    private ITriConsumer<List<A>, List<B>,List<C>> function;
    protected void func(ITriConsumer<List<A>, List<B>,List<C>> function) {
        this.function = function;
    }

    public ITriConsumer<List<A>, List<B>,List<C>> func(){
        return function;
    }

}
