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
import io.cognitionbox.petra.util.impl.PMap;
import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.util.function.IToIntFunction;
import io.cognitionbox.petra.util.Petra;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Map;


import static io.cognitionbox.petra.util.Petra.readConsume;
import static io.cognitionbox.petra.util.Petra.returns;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class Example25Test extends BaseExecutionModesTest {

    public Example25Test(ExecMode execMode) {
        super(execMode);
    }

    public static class TwoIntegerMap extends PMap<Integer,Integer> {
        private final PMap<Integer,Integer> numbers = new PMap<>();
    }

    public static class SumTwoIntegerMap extends PEdge<TwoIntegerMap,Integer> {
        {
            pre(readConsume(TwoIntegerMap.class, a->true));
            func(x->{
                IToIntFunction<Map.Entry<Integer,Integer>> mapper = i->i.getValue().intValue();
                int sumA = x.entrySet().stream().mapToInt(mapper).sum();
                int sumB = x.numbers.entrySet().stream().mapToInt(mapper).sum();
                return sumA+sumB;
            });
            post(Petra.returns(Integer.class, a->true));
        }
    }

    public static class SingleStep extends PGraph<TwoIntegerMap,Integer> {
        {
            pre(readConsume(TwoIntegerMap.class, a->true));
            post(Petra.returns(Integer.class, a->true));
            step(new SumTwoIntegerMap());
        };
    }

    @Test
    public void example25(){


        TwoIntegerMap twoIntegerMap = new TwoIntegerMap();
        twoIntegerMap.put(1,1);
        twoIntegerMap.put(2,2);
        twoIntegerMap.put(3,3);
        twoIntegerMap.numbers.put(4,4);
        twoIntegerMap.numbers.put(5,5);
        twoIntegerMap.numbers.put(6,6);
        Integer result = (Integer) getGraphComputer().computeWithInput(new SingleStep(), twoIntegerMap);
        assertThat(result).isEqualTo(21);
    }


}
