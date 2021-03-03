/**
 * Copyright 2016-2020 Aran Hakki
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cognitionbox.petra.lang.impls;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.lang.PComputer;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.annotations.Extract;
import io.cognitionbox.petra.lang.annotations.SharedResource;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import io.cognitionbox.petra.util.Petra;
import io.cognitionbox.petra.util.impl.PList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.cognitionbox.petra.util.Petra.rw;


@RunWith(Parameterized.class)
public class ResourceConflictTest extends BaseExecutionModesTest {

    public ResourceConflictTest(ExecMode execMode) {
        super(execMode);
    }

    @Extract
    static class BList extends PList<B> {
    }

    @Extract
    public static class A {
        int value = 0;

        @Extract
        private BList b;

        @Override
        public String toString() {
            return "A{" +
                    "value=" + value +
                    '}';
        }

        public A(int value) {
            this.value = value;
        }
    }

    @SharedResource
    public static class B {
        int value = 0;

        @Override
        public String toString() {
            return "B{" +
                    "value=" + value +
                    '}';
        }

        public B(int value) {
            this.value = value;
        }
    }

    public static class AtoA extends PEdge<A> {
        {
            pre(rw(A.class, x -> true));
            func(a -> new A(222));
            post(Petra.rt(A.class, x -> true));
        }
    }

    public static class g extends PGraph<A> {
        {
            pre(rw(A.class, x -> true));
            post(Petra.rt(A.class, x -> true));
            step(AtoA.class);
        }
    }


    @Test(expected = AssertionError.class)
    public void testResourceConflict() {

        PComputer<A> lc = getGraphComputer();
        A result = lc.eval(new g(), new A(1));
    }

}
