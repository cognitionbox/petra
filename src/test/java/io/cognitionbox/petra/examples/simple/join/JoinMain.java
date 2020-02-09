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
        System.setProperty("mode", "PAR");
        System.setProperty("optimizeMode","NONE");

        AB_Result output = new PGraphComputer<AB, AB_Result>().computeWithInput(new ABtoAB(),new AB(new A(),new B()));
        assertThat(output.a.value).isEqualTo(0);
        assertThat(output.b.value).isEqualTo(0);
    }
}