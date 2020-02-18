/**
 * Copyright (C) 2016-2020 Aran Hakki.
 *
 * This file is part of Petra.
 *
 * Petra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Petra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Petra.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.cognitionbox.petra.examples.simple.paralleloop;

import io.cognitionbox.petra.examples.simple.common.A;
import io.cognitionbox.petra.examples.simple.common.AB_Result;
import io.cognitionbox.petra.examples.simple.common.B;
import io.cognitionbox.petra.lang.PJoin2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
       post(returns(AB_Result.class, x->x.a.value==10 && x.b.value==10));
    }
}
