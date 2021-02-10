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
package io.cognitionbox.petra.examples.fizzbuzz;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.lang.AbstractStep;
import io.cognitionbox.petra.lang.StepTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
@RunWith(Parameterized.class)
public class FizzBuzzMain extends StepTest<X> {
    public FizzBuzzMain(ExecMode execMode) {
        super(execMode);
    }

    @Override
    protected Supplier<AbstractStep<X>> stepSupplier() {
        return () -> new FizzBuzzGraph();
    }

    @Test
    public void testSimple() {
        setInput(new X());
        setExpectation(x->x.i==15);
    }
}

