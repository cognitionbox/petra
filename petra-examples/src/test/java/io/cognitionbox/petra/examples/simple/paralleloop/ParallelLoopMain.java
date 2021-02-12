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
package io.cognitionbox.petra.examples.simple.paralleloop;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.examples.simple.common.A;
import io.cognitionbox.petra.examples.simple.common.AB;
import io.cognitionbox.petra.examples.simple.common.B;
import io.cognitionbox.petra.lang.PComputer;
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
     * This is like the LoopMain example but we have two stepForall doing the same thing.
     * Each operating on a separate object, thus Petra automatically parallelizes these stepForall.
     * Instead of returning the result we swallow through use of the optional() function.
     * When the condition of the option is met it rt the actual value, else it will return null,
     * which will be safely swallowed in Petra.
     */
    @Test
    public void test(){
        AB result = new PComputer<AB>().eval(new ABtoAB(),new AB(new A(),new B()));
        assertThat(result.getA().value).isEqualTo(10);
        assertThat(result.getB().value).isEqualTo(10);
    }
}