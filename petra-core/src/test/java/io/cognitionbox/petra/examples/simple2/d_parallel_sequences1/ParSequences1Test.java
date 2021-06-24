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
package io.cognitionbox.petra.examples.simple2.d_parallel_sequences1;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.lang.PComputer;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.RGraphComputer;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ParSequences1Test extends BaseExecutionModesTest {
    public ParSequences1Test(ExecMode execMode) {
        super(execMode);
    }

    @Test
    public void test() {

        class SeqEdge extends PEdge<Y> {
            {
                type(Y.class);
                kase(
                        y -> y.isA() ^ y.isB(),
                        y -> y.isB() ^ y.isC());
                func(y -> {
                    y.state(State.values()[y.state().ordinal() + 1]);
                });
            }
        }

        class SeqGraph extends PGraph<X> {
            {
                type(X.class);
                iterations(x->2);
                kase(
                        x -> (x.y1().isA() ^ x.y1().isB()) && (x.y2().isA() ^ x.y2().isB()),
                        x -> x.y1().isC() && x.y2().isC());
                step(x -> x.y1(), new SeqEdge());
                step(x -> x.y2(), new SeqEdge());
                esak();
            }
        }

        RGraphComputer.getConfig().setIsReachabilityChecksEnabled(false);
        X output = new PComputer<X>().eval(new SeqGraph(), new X(State.A));
        assertThat(output.y1().state()).isEqualTo(State.C);
        assertThat(output.y2().state()).isEqualTo(State.C);

    }
}