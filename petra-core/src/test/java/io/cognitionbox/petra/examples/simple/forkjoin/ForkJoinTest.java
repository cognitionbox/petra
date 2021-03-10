/**
 * Copyright (C) 2016-2020 Aran Hakki.
 * <p>
 * This file is part of Petra.
 * <p>
 * Petra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Petra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Petra.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.cognitionbox.petra.examples.simple.forkjoin;

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
public class ForkJoinTest extends BaseExecutionModesTest {
    public ForkJoinTest(ExecMode execMode) {
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
    public void test() {
        AB output = new PComputer<AB>().eval(new ABtoAB(), new AB(new A(), new B()));
        assertThat(output.getA().value).isEqualTo(10);
        assertThat(output.getB().value).isEqualTo(10);
    }
}