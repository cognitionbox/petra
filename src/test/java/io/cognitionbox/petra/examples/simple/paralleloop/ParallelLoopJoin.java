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
package io.cognitionbox.petra.examples.simple.paralleloop;

import io.cognitionbox.petra.examples.simple.common.A;
import io.cognitionbox.petra.examples.simple.common.AB_Result;
import io.cognitionbox.petra.examples.simple.common.B;
import io.cognitionbox.petra.lang.PJoin2;
import io.cognitionbox.petra.lang.Void;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.cognitionbox.petra.lang.Void.vd;
import static io.cognitionbox.petra.util.Petra.readConsume;
import static io.cognitionbox.petra.util.Petra.returns;


public class ParallelLoopJoin extends PJoin2<A, B, AB_Result> {
    static final Logger LOG = LoggerFactory.getLogger(ParallelLoopJoin.class);
    {
       preA(readConsume(A.class, a->a.value==10));
       preB(readConsume(B.class, b->b.value==10));
       func((a, b)->{
            return new AB_Result(a.get(0),b.get(0));
       });
       post(returns(AB_Result.class,x->x.a.value==10 && x.b.value==10));
    }
}
