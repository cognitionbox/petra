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
package io.cognitionbox.petra.examples.simple.forkjoin;

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
public class ForkJoinMain extends BaseExecutionModesTest {
    public ForkJoinMain(ExecMode execMode) {
        super(execMode);
    }

    /*
     * Traditionally Fork/JoinMain is about forking and joining threads.
     * Petra is a data focused programming paradigm, we are only interested
     * in splitting data and transforming it in parallel where possible, then combining result.
     *
     * The example below shows to steps which get called in parallel repeatably,
     * until a certain condition is met and their results are joined.
     *
     * The aim is to increment X and Y integer values from 0 upto 10 in parallel,
     * then combine X and Y back into a result which can be returned.
     *
     * We do this using a combination of steps and joins.
     * Steps always run first and execute in parallel where possible.
     * Joins always run in the order they are defined, sequentially after all steps finished.
     *
     */
    @Test
    public void test(){
        AB_Result output = (AB_Result) getGraphComputer().computeWithInput(new ABtoAB(),new AB(new A(),new B()));
        assertThat(output.a.value).isEqualTo(10);
        assertThat(output.b.value).isEqualTo(10);
    }
}