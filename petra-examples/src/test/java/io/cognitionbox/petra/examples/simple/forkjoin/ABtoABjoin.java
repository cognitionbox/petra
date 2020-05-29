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
package io.cognitionbox.petra.examples.simple.forkjoin;

import io.cognitionbox.petra.examples.simple.common.A;
import io.cognitionbox.petra.examples.simple.common.AB_Result;
import io.cognitionbox.petra.examples.simple.common.B;
import io.cognitionbox.petra.lang.PJoin2;
import io.cognitionbox.petra.util.Petra;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.cognitionbox.petra.util.Petra.rw;
import static io.cognitionbox.petra.util.Petra.rt;


public class ABtoABjoin extends PJoin2<A, B, AB_Result> {
    static final Logger LOG = LoggerFactory.getLogger(ABtoABjoin.class);
    {
       preA(rw(A.class, a->a.value==10));
       preB(rw(B.class, b->b.value==10));
       func((as, bs)->{
            A a = as.get(0);
            B b = bs.get(0);
            return new AB_Result(a,b);
        });
        post(Petra.rt(AB_Result.class, x->true));
    }
}
