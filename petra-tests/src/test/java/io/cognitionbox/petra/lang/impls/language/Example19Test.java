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

import static io.cognitionbox.petra.util.Petra.rc;
import static io.cognitionbox.petra.util.Petra.rt;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class Example19Test extends BaseExecutionModesTest {

    public Example19Test(ExecMode execMode) {
        super(execMode);
    }

    public static class PEdgeAtoB extends PEdge<A, B> {
        {
            pre(rc(A.class, a->a.getValue().equals("A")));
            func(a->new B("B"));
            post(Petra.rt(B.class, b->b.getValue().equals("B")));
        }
    }
    public static class PEdgeBtoC extends PEdge<B, C> {
        {
            pre(rc(B.class, b->b.getValue().equals("B")));
            func(b->new C("C"));
            post(Petra.rt(C.class, c->c.getValue().equals("C")));
        }
    }
    public static class PGraphAtoB extends PGraph<A,B> {
        {
            pre(rc(A.class, a->a.getValue().equals("A")));
            step(new PEdgeAtoB());
            post(Petra.rt(B.class, b->b.getValue().equals("B")));
        }
    }
    public static class PGraphAtoC extends PGraph<A,C> {
        {
            pre(rc(A.class, a->a.getValue().equals("A")));
            step(new PEdgeBtoC());
            step(new PGraphAtoB());
            post(Petra.rt(C.class, c->c.getValue().equals("C")));
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
