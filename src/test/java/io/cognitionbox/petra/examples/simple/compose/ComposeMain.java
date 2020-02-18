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
package io.cognitionbox.petra.examples.simple.compose;


import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.examples.simple.common.A;
import io.cognitionbox.petra.lang.PGraphComputer;
import io.cognitionbox.petra.lang.Void;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ComposeMain extends BaseExecutionModesTest {
    public ComposeMain(ExecMode execMode) {
        super(execMode);
    }
    /*
     * This demonstrates disorderly programming.
     * The steps can be defined in any order and the outcome is always the same.
     * It shows a more logical way to program.
     *
     * Will print out E and F and then terminate.
     *
     * First A is consumed,
     * then A is transformed to B,
     * then B is transformed to C,
     * then C is tranformed to D (which contains E amd F)
     * then D is deconstucted into E and F
     * then E and F are printed
     * then it terminates
     */

    @Test
    public void test(){
        Void result = (Void) getGraphComputer().computeWithInput(new DependancyGraph(),new A());
        System.out.println(result);
    }
}