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
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.PGraph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class StaticsDetectionTest extends BaseExecutionModesTest {

    // Useful for checking your own code and also third party code used.
    // Petra system does not allow mutable statics.

    public StaticsDetectionTest(ExecMode execMode) {
        super(execMode);
    }

    public static class A {
        int value = 0;
        private static A a = new A(0);

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


    public static class AtoA extends PEdge<A> {
        {
            type(A.class);
            kase(a -> true, a -> true);
            func(a -> new A(222));
        }
    }


    public static class g extends PGraph<A> {
        {
            type(A.class);
            kase(a -> true, a -> true);
                step(AtoA.class);
            esak();
        }
    }


    @Test(expected = AssertionError.class)
    public void testStaticsDetection() {
        A result = (A) getGraphComputer().eval(new g(), new A(1));
    }

}
