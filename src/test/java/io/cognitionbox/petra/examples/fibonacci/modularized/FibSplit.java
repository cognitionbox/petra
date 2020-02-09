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
package io.cognitionbox.petra.examples.fibonacci.modularized;

import io.cognitionbox.petra.examples.fibonacci.IntList;
import io.cognitionbox.petra.examples.fibonacci.IntListEx;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.util.Petra;

import static io.cognitionbox.petra.util.Petra.readConsume;
import static io.cognitionbox.petra.util.Petra.returns;

public class FibSplit extends PEdge<Integer, IntList> {
    {
        pre(readConsume(Integer.class, i->true));
        func(i->{
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
        });
        post(returns(IntList.class, il->il.size()==2));
        post(returns(IntList.class, il->il.size()==1));
    }
}
