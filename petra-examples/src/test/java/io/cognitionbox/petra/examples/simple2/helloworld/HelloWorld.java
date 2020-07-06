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
package io.cognitionbox.petra.examples.simple2.helloworld;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.lang.PComputer;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.cognitionbox.petra.util.Petra.rt;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class HelloWorld extends BaseExecutionModesTest {
    public HelloWorld(ExecMode execMode) {
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

        class AtoA extends PEdge<X> {
            {
                pc(X.class, x -> x.isBlankOrHelloWorld());
                func(x ->{
                    x.value = "hello world.";
                    return x;
                });
                qc(X.class, x -> x.isHelloWorld());
            }
        }

        class AtoAGraph extends PGraph<X> {
            {
                gi(x -> x.isBlankOrHelloWorld());
                pc(X.class, x -> x.isBlank());
                step(new AtoA());
                qc(X.class, x -> x.isHelloWorld());
            }
        }

        X output = new PComputer<X>().eval(new AtoAGraph(),new X(""));
        assertThat(output.value).isEqualTo("hello world.");

    }
}