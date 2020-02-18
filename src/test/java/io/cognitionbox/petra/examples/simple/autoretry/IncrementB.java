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
package io.cognitionbox.petra.examples.simple.autoretry;

import io.cognitionbox.petra.core.IRollback;
import io.cognitionbox.petra.examples.simple.common.B;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.annotations.Feedback;

import static io.cognitionbox.petra.util.Petra.readWrite;
import static io.cognitionbox.petra.util.Petra.returns;

@Feedback
public class IncrementB extends PEdge<B, B> implements IRollback<B> {
    {
       pre(readWrite(B.class, a->a.value<10));
       func(b->{
            if (Math.random()>=0.5){
                throw new IllegalStateException();
            }
            b.value++;
            System.out.println("B="+b.value);
            return b;
        });
        post(returns(B.class,b->b.value==10));
    }

    @Override
    public void capture(B b) {
        b.setCaptured(b.value);
    }
    @Override
    public void rollback(B b) {
        b.value = b.getCaptured();
    }
}