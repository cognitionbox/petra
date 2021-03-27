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
package io.cognitionbox.petra.examples.simple.parallelcalcs;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.config.PetraConfig;
import io.cognitionbox.petra.factory.PetraParallelComponentsFactory;
import io.cognitionbox.petra.factory.PetraSequentialComponentsFactory;
import io.cognitionbox.petra.lang.PComputer;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ParallelCalcsTest extends BaseExecutionModesTest {


    public ParallelCalcsTest(ExecMode execMode) {
        super(execMode);
    }

    @Test
    public void test() {

        PComputer<Calculations> computer = new PComputer();

        Calculations output = computer.eval(new ParCalculator(), new Calculations());

        System.out.println(output.addPositiveNumbers1.result);
        System.out.println(output.addPositiveNumbers2.result);
        assertThat(output.addPositiveNumbers1.result.get()).isEqualTo(11);
        assertThat(output.addPositiveNumbers2.result.get()).isEqualTo(11);
    }
}
