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
package io.cognitionbox.petra.examples.simple.extraction;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.examples.simple.common.A;
import io.cognitionbox.petra.examples.simple.common.AB;
import io.cognitionbox.petra.examples.simple.common.B;
import io.cognitionbox.petra.lang.PGraphComputer;
import io.cognitionbox.petra.lang.Void;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


@RunWith(Parameterized.class)
public class ExtractionMain extends BaseExecutionModesTest {
    public ExtractionMain(ExecMode execMode) {
        super(execMode);
    }
    /*
     * This a simple extraction.
     * The class XY is defined to contain X and Y references.
     * The XY class itself is annotated with @Extract instructing
     * the runtime to deconstruct it into its constituents.
     * This will cause it to reflectively traverse the object engine looking for
     * fields annotated with @Extract.
     *
     * In this example there are just two fields X and Y with this annotation.
     * The deconstruction of XY into X and Y means that X and Y become available for
     * being matched by steps in the engine. Hence they can be processed in parallel.
     * In this example we simply print out the X and Y instances.
     */
    @Test
    public void test(){
       Void result = (Void) getGraphComputer().computeWithInput(new ExtractAB(),new AB(new A(),new B()));
    }
}
