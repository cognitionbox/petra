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
