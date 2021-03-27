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
package io.cognitionbox.petra.lang.impls.hawkeye.steps.hawkeye5;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.lang.AbstractStep;
import io.cognitionbox.petra.lang.StepTest;
import io.cognitionbox.petra.lang.impls.hawkeye.objects.X;
import io.cognitionbox.petra.lang.impls.hawkeye.steps.hawkeye2.HawkeyeGraph2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.function.Supplier;

public class LoopIterationTest extends StepTest<Foo> {

    @Override
    protected Class<? extends IStep<Foo>> stepClass() {
        return SomeLoop.class;
    }

    @Test
    public void test(){
        setInput(new Foo(Arrays.asList("a","b","lastone")));
        setExpectation(x->true);
    }

    @Test
    public void test1(){
        setInput(new Foo(Arrays.asList("a")));
        setExpectation(x->true);
    }
}