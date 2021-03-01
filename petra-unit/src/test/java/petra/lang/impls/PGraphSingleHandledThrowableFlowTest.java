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
package petra.lang.impls;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class PGraphSingleHandledThrowableFlowTest extends BaseExecutionModesTest {


    public PGraphSingleHandledThrowableFlowTest(ExecMode execMode) {
        super(execMode);
    }

    static class X {
        Integer value;

        public X(Integer value) {
            this.value = value;
        }
    }

    @Test
    public void testThrowableFlow() {

        X result = (X) getGraphComputer().eval(new MainLoop(), new X(0));
        assertThat(result).isInstanceOf(X.class);
        assertThat(result.value).isEqualTo(3);
    }

    @Test
    public void testThrowableFlowWithDirectStepHandledThrowable() {

        X result = (X) getGraphComputer().eval(new MainLoopWithDirectStepHandledThrowable(), new X(0));
        assertThat(result).isInstanceOf(X.class);
        assertThat(result.value).isEqualTo(3);
    }

    public static class MainLoop extends PGraph<X> {
        {
            type(X.class);
            pre(x -> x.value == 0);
            begin();
            step(new Nesting());
            end();
            post(x -> x.value == 3);
        }
    }

    public static class Nesting extends PGraph<X> {
        {
            type(X.class);
            pre(x -> x.value == 0);
            begin();
            step(new PlusOne());
            end();
            post(x -> x.value == 1 || x.value == 3);
        }
    }

    public static class MainLoopWithDirectStepHandledThrowable extends PGraph<X> {
        {
            type(X.class);
            pre(x -> x.value == 0);
            begin();
            step(new NestingWithDirectStepHandledThrowable());
            end();
            post(x -> x.value == 3);
        }
    }

    public static class NestingWithDirectStepHandledThrowable extends PGraph<X> {
        {
            type(X.class);
            pre(x -> x.value == 0);
            begin();
            step(new PlusOne());
            end();
            post(x -> x.value == 3);

        }
    }

    public static class PlusOne extends PEdge<X> {
        {
            type(X.class);
            pre(x -> x.value == 0);
            post(x -> x.value == 1 || x.value == 3);
            func(x -> {
                try {
                    int y = 1 / 0;
                } catch (ArithmeticException e) {
                    x.value = 3;
                }
            });
        }
    }
}
