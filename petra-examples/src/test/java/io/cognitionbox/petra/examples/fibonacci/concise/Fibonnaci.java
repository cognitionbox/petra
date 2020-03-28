/**
 * Copyright (C) 2016-2020 Aran Hakki.
 * <p>
 * This file is part of Petra.
 * <p>
 * Petra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Petra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Petra.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.cognitionbox.petra.examples.fibonacci.concise;

import io.cognitionbox.petra.factory.PetraParallelComponentsFactory;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.PGraphComputer;
import io.cognitionbox.petra.lang.RGraphComputer;
import io.cognitionbox.petra.lang.annotations.Extract;
import io.cognitionbox.petra.util.impl.PList;

import static io.cognitionbox.petra.util.Petra.*;

public class Fibonnaci {

    public static void main(String... args) {
        RGraphComputer.getConfig().setParallelModeFactory(new PetraParallelComponentsFactory());
        RGraphComputer.getConfig().enableStatesLogging();

        class IntList extends PList<Integer> {
        }
        @Extract class IntListEx extends IntList {
        }

        class Fibonacci extends PGraph<Integer, IntList> {
            {
                pre(rc(Integer.class, a -> true));
                step(anonymous(rc(Integer.class, x -> true), i -> {
                    IntList il = null;
                    if (i < 2) {
                        il = new IntList();
                        il.add(i);
                    } else {
                        il = new IntListEx();
                        il.add(i - 1);
                        il.add(i - 2);
                    }
                    return il;
                }, rt(IntList.class, x -> true)));
                joinAll(anonymousJ1(rc(IntList.class, i -> i.size() == 1), i -> {
                    IntList il = new IntList();
                    il.add(i.stream().flatMap(x -> x.stream()).mapToInt(y -> y).sum());
                    return il;
                }, rt(IntList.class, i -> true)));
                post(rt(IntList.class, i -> i.size() == 1));
            }
        }

        PGraphComputer<Integer, IntList> lc = new PGraphComputer();
        IntList res = lc.computeWithInput(new Fibonacci(), 8);
        System.out.println(res.get(0));
    }
}
