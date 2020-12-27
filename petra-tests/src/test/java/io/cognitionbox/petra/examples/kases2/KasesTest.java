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
package io.cognitionbox.petra.examples.kases2;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.examples.kases2.objects.Foo;
import io.cognitionbox.petra.examples.kases2.steps.FooSum2;
import io.cognitionbox.petra.lang.PComputer;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigDecimal;
import java.util.Arrays;

@RunWith(Parameterized.class)
public class KasesTest extends BaseExecutionModesTest {
    public KasesTest(ExecMode execMode) {
        super(execMode);
    }

    @Test
    public void test3() {
        PComputer.getConfig()
                .enableStatesLogging()
                .setMode(ExecMode.PAR)
                .setConstructionGuaranteeChecks(false)
                .setStrictModeExtraConstructionGuarantee(false);

        PComputer<Foo> lc = new PComputer();

        Foo foo = new Foo();

        Foo output = lc.eval(new FooSum2(), foo);
        System.out.println(output);
    }
}
