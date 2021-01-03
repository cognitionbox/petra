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
package io.cognitionbox.petra.examples.kases5;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.examples.kases.objects.Foo;
import io.cognitionbox.petra.examples.kases.steps.FooSum;
import io.cognitionbox.petra.examples.kases5.objects.SystemState;
import io.cognitionbox.petra.examples.kases5.steps.SystemGraph;
import io.cognitionbox.petra.lang.AbstractStep;
import io.cognitionbox.petra.lang.PComputer;
import io.cognitionbox.petra.lang.StepTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.function.Supplier;


@RunWith(Parameterized.class)
public class KasesStepTest5 extends StepTest<Foo> {
    public KasesStepTest5(ExecMode execMode) {
        super(execMode);
    }

    protected Supplier<AbstractStep<Foo>> stepSupplier(){
        return ()->new FooSum();
    }


    @Test
    public void test() {
        Foo foo = new Foo();
        foo.setRList(Arrays.asList(new BigDecimal(1),new BigDecimal(2),new BigDecimal(3)));

        setInput(foo);
        setExpectation(x->true);
    }

    @Test
    public void test2() {
        Foo foo = new Foo();
        foo.setRList(Arrays.asList(new BigDecimal(1)));

        setInput(foo);
        setExpectation(x->true);
    }
}
