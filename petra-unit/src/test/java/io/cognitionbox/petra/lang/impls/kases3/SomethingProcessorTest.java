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
package io.cognitionbox.petra.lang.impls.kases3;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.lang.StepTest;
import io.cognitionbox.petra.lang.impls.generics.X;
import io.cognitionbox.petra.lang.impls.generics.XtoX;
import io.cognitionbox.petra.lang.impls.kases3.objects.Something;
import io.cognitionbox.petra.lang.impls.kases3.steps.SomethingProcessor;
import org.junit.Test;

public class SomethingProcessorTest extends StepTest<Something> {
    @Override
    protected Class<? extends IStep<Something>> stepClass() {
        return SomethingProcessor.class;
    }

    @Test
    public void test1() {
        setInput(new Something(1));
        setExpectation(x->true);
    }

    @Test
    public void test2() {
        setInput(new Something(6));
        setExpectation(x->true);
    }

//    @Test
//    public void test3() {
//        setInput(new Something(11));
//        setExpectation(i->true);
//    }
}
