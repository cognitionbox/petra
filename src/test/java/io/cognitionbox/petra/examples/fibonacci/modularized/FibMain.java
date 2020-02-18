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
package io.cognitionbox.petra.examples.fibonacci.modularized;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.examples.fibonacci.IntList;
import io.cognitionbox.petra.lang.PGraphComputer;
import io.cognitionbox.petra.lang.RGraphComputer;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class FibMain extends BaseExecutionModesTest {

    public FibMain(ExecMode execMode) {
        super(execMode);
    }

    @Test
    public void test() {
        getGraphComputer().getConfig().setConstructionGuaranteeChecks(false);
        getGraphComputer().getConfig().setStrictModeExtraConstructionGuarantee(false);

        IntList res = (IntList) getGraphComputer().computeWithInput(new Fibonacci(), 8);
        System.out.println(res.get(0));
        assertThat(res.get(0)).isEqualTo(21);
    }
}
