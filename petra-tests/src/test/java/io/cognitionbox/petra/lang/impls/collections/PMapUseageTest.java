/**
 * Copyright 2016-2020 Aran Hakki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cognitionbox.petra.lang.impls.collections;

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


import static io.cognitionbox.petra.util.Petra.rc;
import static io.cognitionbox.petra.util.Petra.rt;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class PMapUseageTest extends BaseExecutionModesTest {

  public PMapUseageTest(ExecMode execMode) {
    super(execMode);
  }

  public static class TwoIntegerMap extends PMap<Integer,Integer> {
    private final PMap<Integer,Integer> numbers = new PMap<>();
  }
  @Test
  public void testSingleStep() {
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

  public static class SumTwoIntegerMap extends PEdge<TwoIntegerMap,Integer> {
    SumTwoIntegerMap(){
      pre(rc(TwoIntegerMap.class, x->true));
      func(x->{
        IToIntFunction<Map.Entry<Integer,Integer>> mapper = i->i.getValue().intValue();
        int sumA = x.entrySet().stream().mapToInt(mapper).sum();
        int sumB = x.numbers.entrySet().stream().mapToInt(mapper).sum();
        return sumA+sumB;
      });
      post(Petra.rt(Integer.class, x->true));
    }
  }

  public static class SingleStep extends PGraph<TwoIntegerMap,Integer> {
    SingleStep(){
      pre(rc(TwoIntegerMap.class, x->true));
      post(Petra.rt(Integer.class, x->true));
      step(new SumTwoIntegerMap());
    };
  }

}
