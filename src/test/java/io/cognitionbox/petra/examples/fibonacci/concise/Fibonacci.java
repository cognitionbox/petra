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
package io.cognitionbox.petra.examples.fibonacci.concise;

import io.cognitionbox.petra.examples.fibonacci.IntList;
import io.cognitionbox.petra.examples.fibonacci.IntListEx;
import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.*;

public class Fibonacci extends PGraph<Integer, IntList> {
    {
        pre(readConsume(Integer.class, a->true));
        step(anonymous(readConsume(Integer.class, x->true), i->{
            IntList il = null;
            if (i<2){
                il = new IntList();
                il.add(i);
            } else {
                il = new IntListEx();
                il.add(i-1);
                il.add(i-2);
            }
            return il;
        }, returns(IntList.class, x->true)));
        joinAll(anonymousJ1(readConsume(IntList.class, i->i.size()==1), i->{
            IntList il = new IntList();
            il.add(i.stream().flatMap(x->x.stream()).mapToInt(y->y).sum());
            return il;
        }, returns(IntList.class, i->true)));
        post(returns(IntList.class, i->i.size()==1));
    }
}
