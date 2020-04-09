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
import io.cognitionbox.petra.lang.AbstractJoin2;
import io.cognitionbox.petra.lang.AbstractPureJoin;
import io.cognitionbox.petra.lang.Guard;
import io.cognitionbox.petra.lang.GuardInput;
import io.cognitionbox.petra.util.function.IBiConsumer;
import io.cognitionbox.petra.util.function.IBiFunction;

import java.util.List;

public abstract class AbstractEffectJoin2<A,B> extends AbstractJoin2<A,B> implements IJoin {

    IBiConsumer<List<A>, List<B>> function;
    private Guard<? super B> b;
    private Guard<? super A> a;

    Guard<? super B> postB;
    Guard<? super A> postA;

    AbstractEffectJoin2(Guard<? super A> a, Guard<? super B> b,
                        IBiConsumer<List<A>, List<B>> function) {
        this.a = a;
        this.b = b;
        this.function = function;
    }

    protected AbstractEffectJoin2() {
    }

    public Guard<? super B> b() {
        return b;
    }

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

    protected void postA(GuardReturn<? super A> a) {
        this.postA = a;
    }

    protected void postB(GuardReturn<? super B> b) {
        this.postB = b;
    }

    public IBiConsumer<List<A>, List<B>> func(){
        return function;
    }
    public void func(IBiConsumer<List<A>, List<B>> function) {
        this.function = function;
    }

}
