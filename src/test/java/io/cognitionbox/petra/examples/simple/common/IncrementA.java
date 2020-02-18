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
package io.cognitionbox.petra.examples.simple.common;

import io.cognitionbox.petra.core.IRollback;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.annotations.Feedback;

import static io.cognitionbox.petra.util.Petra.*;


@Feedback
public class IncrementA extends PEdge<A,A> implements IRollback<A> {
    {
       pre(readWrite(A.class, a->a.value<10));
       func(a->{
            a.value++;
            System.out.println("X="+a.value);
            return a;
       });
       post(returns(A.class,x->x.value==10));
    }

    @Override
    public void capture(A input) {

    }

    @Override
    public void rollback(A input) {

    }
}