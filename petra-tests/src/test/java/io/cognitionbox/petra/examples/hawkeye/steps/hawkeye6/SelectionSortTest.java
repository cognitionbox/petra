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
package io.cognitionbox.petra.examples.hawkeye.steps.hawkeye6;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.examples.hawkeye.steps.hawkeye6.objects.Outer;
import io.cognitionbox.petra.examples.hawkeye.steps.hawkeye6.steps.SortOuterLoop;
import io.cognitionbox.petra.lang.AbstractStep;
import io.cognitionbox.petra.lang.StepTest;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.function.Supplier;

import static io.cognitionbox.petra.util.Petra.rw;
import static java.util.stream.Collectors.toList;

@Ignore
@RunWith(Parameterized.class)
public class SelectionSortTest extends StepTest<Outer> {
    public SelectionSortTest(ExecMode execMode) {
        super(execMode);
    }

    @Override
    protected Supplier<AbstractStep<Outer>> stepSupplier() {
        return ()->new SortOuterLoop();
    }

    @Test
    public void test(){
        setInput(new Outer(Arrays.asList(rw(3),rw(1),rw(2))));
        setExpectation(x->x.integers.equals(Arrays.asList(rw(1),rw(2),rw(3))));
    }

//    @Test
//    public void test1(){
//        setInput(new Outer(Arrays.asList(rw(2),rw(1),rw(3))));
//        setExpectation(x->true);
//    }
}