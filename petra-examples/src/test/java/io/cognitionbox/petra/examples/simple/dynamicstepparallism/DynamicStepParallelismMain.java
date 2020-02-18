/**
 * Copyright (C) 2016-2020 Aran Hakki.
 *
 * This file is part of Petra.
 *
 * Petra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Petra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Petra.  If not, see <https://www.gnu.org/licenses/>.
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
     * PSet, PMap etc. These collections can be used in SEQ, PAR and DIS modes out the box.
     * (DIS mode requires the common contained by the collections to be Serializable).
     * In order for a collection to be deconstructed it must be extended and its extension
     * must be marked @Extract. Future versions will allow @Extract to be used directly on
     * the collections with out the need for extended them.
     *
     * X will be matched by PrintA and then be deconstructed into 6 A's.
     * 2 from the A references directly referenced in X and 4 within the list of A's referenced by X.
     * Resulting in 6 A's being printed.
     */
    @Test
    public void test(){
        Void result = new PGraphComputer<X, Void>().computeWithInput(new PrintAGraph(),new X());
    }
}