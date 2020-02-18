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
package io.cognitionbox.petra.examples.simple.loop;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.lang.PGraphComputer;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class LoopMain extends BaseExecutionModesTest {
    public LoopMain(ExecMode execMode) {
        super(execMode);
    }
    /*
     * This shows how to create a simple loop directly in Petra.
     * We have a simple engine which consumes X when its integer value is less than 10.
     * The step inside it also takes X, there is no need to put the same constrain as before as
     * X has already passed the initial constraint. No this step can increment X's integer by 1,
     * and return X.
     * Once X values reaches 10, it will be returned by the produces post-condition,
     * hence the loop terminates.
     */
    @Test
    public void test(){
        A output = (A) getGraphComputer().computeWithInput(new AtoA(),new A());
        System.out.println("OUTPUT: "+output.value);
        assertThat(output.value).isEqualTo(10);
    }
}