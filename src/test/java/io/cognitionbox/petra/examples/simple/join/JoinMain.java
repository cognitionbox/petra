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
package io.cognitionbox.petra.examples.simple.join;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.examples.simple.common.A;
import io.cognitionbox.petra.examples.simple.common.AB;
import io.cognitionbox.petra.examples.simple.common.AB_Result;
import io.cognitionbox.petra.examples.simple.common.B;
import io.cognitionbox.petra.lang.PGraphComputer;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class JoinMain extends BaseExecutionModesTest {
    public JoinMain(ExecMode execMode) {
        super(execMode);
    }
    /*
     * Joins in Petra are about data.
     * This example shows how an separate common X and Y can be
     * combined into a single return type by use of joins.
     * Joins always run in the order they are defined, sequentially after all steps finished.
     * In this example there are no steps. All that happens is the input XY is deconstructed into X and Y,
     * then recombined into a result type that cannot be deconstructed so it can be returned.
     */
    @Test
    public void test(){
        AB_Result output = (AB_Result) getGraphComputer().computeWithInput(new ABtoAB(),new AB(new A(),new B()));
        assertThat(output.a.value).isEqualTo(0);
        assertThat(output.b.value).isEqualTo(0);
    }
}