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
package io.cognitionbox.petra.examples.hawkeye.steps.hawkeye4;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.lang.AbstractStep;
import io.cognitionbox.petra.lang.StepTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.function.Supplier;

@RunWith(Parameterized.class)
public class HawkeyeTest4 extends StepTest<X> {
    public HawkeyeTest4(ExecMode execMode) {
        super(execMode);
    }

    protected Supplier<AbstractStep<X>> stepSupplier(){
        return ()->new HawkeyeGraph4();
    }

    @Test
    public void test1() {
        setInput(new X(0.5));
        setExpectation(i->true);
    }

}
