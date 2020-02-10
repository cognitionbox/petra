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
import io.cognitionbox.petra.util.impl.PList;
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
public class Example24Test extends BaseExecutionModesTest {

    public Example24Test(ExecMode execMode) {
        super(execMode);
    }

    public static class TwoIntegerLists extends PList<Integer> {
        private final PList<Integer> numbers = new PList<>();
    }

    public static class SumTwoIntegerLists extends PEdge<TwoIntegerLists,Integer> {
        {
            pre(readConsume(TwoIntegerLists.class, x->true));
            func(x->{
                IToIntFunction<Integer> mapper = i->i.intValue();
                int sumA = x.stream().mapToInt(mapper).sum();
                int sumB = x.numbers.stream().mapToInt(mapper).sum();
                return sumA+sumB;
            });
            post(returns(Integer.class, x->true));
        }
    }

    public static class SingleStep extends PGraph<TwoIntegerLists,Integer> {
        {
            pre(readConsume(TwoIntegerLists.class, x->true));
            post(returns(Integer.class, x->true));
            step(new SumTwoIntegerLists());
        };
    }

    @Test
    public void example24(){
        TwoIntegerLists twoIntegerLists = new TwoIntegerLists();
        twoIntegerLists.add(1);
        twoIntegerLists.add(2);
        twoIntegerLists.add(3);
        twoIntegerLists.numbers.add(4);
        twoIntegerLists.numbers.add(5);
        twoIntegerLists.numbers.add(6);
        Integer result = (Integer) getGraphComputer().computeWithInput(new SingleStep(),twoIntegerLists);
        assertThat(result).isEqualTo(21);
    }

}
