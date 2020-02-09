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
package io.cognitionbox.petra.lang.impls.language;

import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.util.impl.PSet;
import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.util.function.IToIntFunction;
import io.cognitionbox.petra.util.Petra;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


import static io.cognitionbox.petra.util.Petra.readConsume;
import static io.cognitionbox.petra.util.Petra.returns;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class Example26Test extends BaseExecutionModesTest {


    public Example26Test(ExecMode execMode) {
        super(execMode);
    }

    static class TwoIntegerSets extends PSet<Integer> {
        private final PSet<Integer> numbers = new PSet<>();
    }

    public static class SumTwoIntegerSets extends PEdge<TwoIntegerSets,Integer> {
        {
            pre(readConsume(TwoIntegerSets.class, x->true));
            func(x->{
                IToIntFunction<Integer> mapper = i->i.intValue();
                int sumA = x.pstream().mapToInt(mapper).sum();
                int sumB = x.numbers.pstream().mapToInt(mapper).sum();
                return sumA+sumB;
            });
            post(Petra.returns(Integer.class, x->true));
        }
    }

    public static class SingleStep extends PGraph<TwoIntegerSets,Integer> {
        {
            pre(readConsume(TwoIntegerSets.class, x->true));
            post(Petra.returns(Integer.class, x->true));
            step(new SumTwoIntegerSets());
        };
    }

    @Test
    public void example26(){


        TwoIntegerSets twoIntegerSets = new TwoIntegerSets();
        twoIntegerSets.add(1);
        twoIntegerSets.add(2);
        twoIntegerSets.add(3);
        twoIntegerSets.numbers.add(4);
        twoIntegerSets.numbers.add(5);
        twoIntegerSets.numbers.add(6);
        Integer result = (Integer) getGraphComputer().computeWithInput(new SingleStep(), twoIntegerSets);
        assertThat(result).isEqualTo(21);
    }

}
