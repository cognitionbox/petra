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
package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.config.ExecMode;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.function.Supplier;

@Ignore
@RunWith(Parameterized.class)
public class AtoBStepTest extends StepTest<Aok,Bok>{
    public AtoBStepTest(ExecMode execMode) {
        super(execMode);
    }

    @Override
    Supplier<AbstractStep<Aok,Bok>> stepSupplier() {
        return ()->new AtoB();
    }

    @Test
    public void test1(){
        setInput(new A());
        setExpectation(b->b instanceof B);
    }
}
