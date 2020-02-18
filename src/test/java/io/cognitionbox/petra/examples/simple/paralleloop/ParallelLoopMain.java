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
package io.cognitionbox.petra.examples.simple.paralleloop;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.examples.simple.common.A;
import io.cognitionbox.petra.examples.simple.common.AB;
import io.cognitionbox.petra.examples.simple.common.AB_Result;
import io.cognitionbox.petra.examples.simple.common.B;
import io.cognitionbox.petra.lang.PGraphComputer;
import io.cognitionbox.petra.lang.Void;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ParallelLoopMain extends BaseExecutionModesTest {
    public ParallelLoopMain(ExecMode execMode) {
        super(execMode);
    }
    /*
     * This is like the LoopMain example but we have two steps doing the same thing.
     * Each operating on a separate object, thus Petra automatically parallelizes these steps.
     * Instead of returning the result we swallow through use of the optional() function.
     * When the condition of the option is met it returns the actual value, else it will return null,
     * which will be safely swallowed in Petra.
     */
    @Test
    public void test(){
        AB_Result result = (AB_Result) getGraphComputer().computeWithInput(new ABtoAB(),new AB(new A(),new B()));
        assertThat(result.a.value).isEqualTo(10);
        assertThat(result.b.value).isEqualTo(10);
    }
}