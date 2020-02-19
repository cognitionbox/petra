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
