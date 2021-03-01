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
package io.cognitionbox.petra.examples.simple2.c_sequence2;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.lang.PComputer;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class Sequence2 extends BaseExecutionModesTest {
    public Sequence2(ExecMode execMode) {
        super(execMode);
    }
    /*
     * Laws:
     *
     * Only for the Graphs:
     *
     * POST => PRE
     * LC => PRE
     * POST <=> ¬LC
     *
     *
     * Only for Edges:
     *
     * PRE </=> POST
     */
    @Test
    public void test(){

        class SeqEdge1 extends PEdge<X> {
            {
                type(X.class);
                pre(x -> x.isA());
                func(x ->{
                    x.state(State.B);
                });
                post(x -> x.isB());
            }
        }
        class SeqEdge2 extends PEdge<X> {
            {
                type(X.class);
                pre(x ->x.isB());
                func(x ->{
                    x.state(State.C);
                });
                post(x -> x.isC());
            }
        }

        class SeqGraph extends PGraph<X> {
            {
                type(X.class);
                pre(x->x.isAB());
                begin();
                step(new SeqEdge2());
                step(new SeqEdge1());
                end();
                post(x->x.isC());
            }
        }

        X output = new PComputer<X>().eval(new SeqGraph(),new X(State.A));
        assertThat(output.state()).isEqualTo(State.C);


    }
}