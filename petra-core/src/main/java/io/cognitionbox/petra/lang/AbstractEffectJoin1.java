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
import io.cognitionbox.petra.util.function.IConsumer;
import io.cognitionbox.petra.util.function.IFunction;

import java.util.List;

public abstract class AbstractEffectJoin1<A> extends AbstractJoin1<A> implements IJoin {

    Guard<? super A> a;
    Guard<? super A> postA;

    AbstractEffectJoin1(Guard<? super A> a, IConsumer<List<A>> function) {
        this.a = a;
        this.function = function;
    }

    protected AbstractEffectJoin1() {}

    public Guard<? super A> a() {
        return a;
    }

    protected void pre(GuardInput<? super A> a) {
        addInputType(a);
        this.a = a;
    }

    protected void post(GuardReturn<? super A> a) {
        this.postA = a;
    }

    private long millisBeforeRetry = 100;
    public long getMillisBeforeRetry() {
        return millisBeforeRetry;
    }

    protected void setMillisBeforeRetry(long millisBeforeRetry) {
        this.millisBeforeRetry = millisBeforeRetry;
    }

//    abstract IFunction func();

    public IConsumer<List<A>> func() {
        return function;
    }

    IConsumer<List<A>> function;

    public void func(IConsumer<List<A>> function) {
        this.function = function;
    }
}
