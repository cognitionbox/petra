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
import io.cognitionbox.petra.core.IJoin3;
import io.cognitionbox.petra.util.function.ITriFunction;

import java.util.List;

@Pure
public class PJoin3<A,B,C,R> extends AbstractJoin3<A,B,C,R> implements IJoin3<A, B, C, R> {
    public PJoin3(){}
    public PJoin3(Guard<? super A> a, Guard<? super B> b, Guard<? super C> c, ITriFunction<List<A>, List<B>, List<C>, R> function, Guard<? super R> r) {
        super(a, b, c, function, r);
    }
}
