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
package io.cognitionbox.petra.examples.simple.extraction;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.examples.simple.common.A;
import io.cognitionbox.petra.examples.simple.common.AB;
import io.cognitionbox.petra.examples.simple.common.B;
import io.cognitionbox.petra.lang.PComputer;
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
     * The class AB is defined to contain A and B references.
     * The AB class itself is annotated with @Extract instructing
     * the runtime to deconstruct it into its constituents.
     * This will cause it to reflectively traverse the object engine looking for
     * fields annotated with @Extract.
     *
     * In this example there are just two fields A and B with this annotation.
     * The deconstruction of AB into A and B means that A and B become available for
     * being matched by steps in the engine. Hence they can be processed in parallel.
     * In this example we simply print out the A and B instances.
     */
    @Test
    public void test(){
        AB result = new PComputer<AB, AB>().eval(new ExtractAB(),new AB(new A(),new B()));
    }
}
