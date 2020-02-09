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
package io.cognitionbox.petra.lang.impls.language;

import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.impls.language.types.A;
import io.cognitionbox.petra.lang.impls.language.types.B;
import io.cognitionbox.petra.lang.impls.language.types.C;
import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.util.Petra;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.cognitionbox.petra.util.Petra.readConsume;
import static io.cognitionbox.petra.util.Petra.returns;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class Example19Test extends BaseExecutionModesTest {

    public Example19Test(ExecMode execMode) {
        super(execMode);
    }

    public static class PEdgeAtoB extends PEdge<A, B> {
        {
            pre(readConsume(A.class, a->a.getValue().equals("A")));
            func(a->new B("B"));
            post(Petra.returns(B.class, b->b.getValue().equals("B")));
        }
    }
    public static class PEdgeBtoC extends PEdge<B, C> {
        {
            pre(readConsume(B.class, b->b.getValue().equals("B")));
            func(b->new C("C"));
            post(Petra.returns(C.class, c->c.getValue().equals("C")));
        }
    }
    public static class PGraphAtoB extends PGraph<A,B> {
        {
            pre(readConsume(A.class, a->a.getValue().equals("A")));
            step(new PEdgeAtoB());
            post(Petra.returns(B.class, b->b.getValue().equals("B")));
        }
    }
    public static class PGraphAtoC extends PGraph<A,C> {
        {
            pre(readConsume(A.class, a->a.getValue().equals("A")));
            step(new PEdgeBtoC());
            step(new PGraphAtoB());
            post(Petra.returns(C.class, c->c.getValue().equals("C")));
        }
    }

    @Test
    public void example19(){
        // Example 19. Same is Example 8 but using constructor pattern rather than the builder pattern as in all previous examples.
        // Constructor pattern is where all pre/post conditions and steps are defined in the constructor of a PEdge, PGraph and XGraphSafe
        // extension.

        C result19 = (C) getGraphComputer()
                .computeWithInput(new PGraphAtoC(),new A("A"));
        assertThat(result19).isInstanceOf(C.class);
    }

}
