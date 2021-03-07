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
package io.cognitionbox.petra.examples.simple.helloworld;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.lang.PComputer;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class HelloWorldMain extends BaseExecutionModesTest {
    public HelloWorldMain(ExecMode execMode) {
        super(execMode);
    }

    /*
     * This simple example consumes an input A,
     * does nothing with it and passes it straight
     * through to the the post-condition.
     *
     * Once it is output from the root of the engine A's message is printed.
     */
    @Test
    public void test() {
        A output = new PComputer<A>().eval(new AtoAGraph(), new A(""));
        System.out.println("OUTPUT: " + output.value);
        assertThat(output.value).isEqualTo("hello world.");
    }
}