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
package io.cognitionbox.petra.examples.simple.compose;


import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.lang.PComputer;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ComposeMain extends BaseExecutionModesTest {
    public ComposeMain(ExecMode execMode) {
        super(execMode);
    }
    /*
     * This demonstrates disorderly programming.
     * The steps can be defined in any order and the outcome is always the same.
     * It shows a more logical way to program.
     *
     * Will print out E and F and then terminate.
     *
     * First A is consumed,
     * then A is transformed to B,
     * then B is transformed to C,
     * then C is tranformed to D (which contains E amd F)
     * then D is deconstucted into E and F
     * then E and F are printed
     * then it terminates
     */

    @Test
    public void test(){
        PComputer.getConfig().enableStatesLogging();
        X result = new PComputer<X, X>().eval(new DependancyGraph(),new X());
        System.out.println(result);
    }
}