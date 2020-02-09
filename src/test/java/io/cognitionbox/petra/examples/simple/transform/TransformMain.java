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
package io.cognitionbox.petra.examples.simple.transform;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.examples.simple.common.A;
import io.cognitionbox.petra.examples.simple.common.B;
import io.cognitionbox.petra.lang.PGraphComputer;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class TransformMain extends BaseExecutionModesTest {
    public TransformMain(ExecMode execMode) {
        super(execMode);
    }
    /*
     * A simple example showing how an object of one type is mapped into another.
     */
    @Test
    public void test(){
        System.setProperty("mode", "PAR");
        System.setProperty("optimizeMode","NONE");

        B output = new PGraphComputer<A, B>().computeWithInput(new AtoBGraph(),new A());
        System.out.println("OUTPUT: "+output.getClass().getSimpleName());
        assertThat(output).isInstanceOf(B.class);
    }
}