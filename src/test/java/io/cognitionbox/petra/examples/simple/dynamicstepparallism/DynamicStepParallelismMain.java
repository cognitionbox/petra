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
package io.cognitionbox.petra.examples.simple.dynamicstepparallism;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.lang.PGraphComputer;
import io.cognitionbox.petra.lang.Void;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class DynamicStepParallelismMain extends BaseExecutionModesTest {
    public DynamicStepParallelismMain(ExecMode execMode) {
        super(execMode);
    }

    /*
     * Demonstrates Dynamic Step Parallelism through different type of extractions...
     *
     * X is marked @Extract and contains two references to A, which is also marked @Extract.
     * X also contains a reference to a list marked @Extract.
     * This list an extension of PList.
     * PList is part of a family of Petra cross-runtime-mode collections, including:
     * XSet, XMap etc. These collections can be used in SEQ, PAR and DIS modes out the box.
     * (DIS mode requires the common contained by the collections to be Serializable).
     * In order for a collection to be deconstructed it must be extended and its extension
     * must be marked @Extract. Future versions will allow @Extract to be used directly on
     * the collections with out the need for extended them.
     *
     * X will be matched by PrintA and then be deconstructed into 6 A's.
     * 2 from the A references directly referenced in X and 4 within the list of A's referenced by X.
     *
     */
    @Test
    public void test(){
        Void result = (Void) getGraphComputer().computeWithInput(new PrintAGraph(),new X());
    }
}