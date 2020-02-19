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
