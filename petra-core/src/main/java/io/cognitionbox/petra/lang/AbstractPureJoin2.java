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
import io.cognitionbox.petra.util.function.IBiFunction;

import java.util.List;

public abstract class AbstractPureJoin2<A,B,R> extends AbstractPureJoin<R> implements IJoin {

    private Guard<? super B> b;

    AbstractPureJoin2(Guard<? super A> a, Guard<? super B> b,
                      IBiFunction<List<A>, List<B>,R> function,
                      Guard<? super R> r) {
        super(r);
        this.a = a;
        this.b = b;
        this.function = function;
    }

    protected AbstractPureJoin2() {
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
