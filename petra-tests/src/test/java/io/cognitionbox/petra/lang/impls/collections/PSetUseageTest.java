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
import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.util.impl.PSet;
import io.cognitionbox.petra.util.function.IToIntFunction;
import io.cognitionbox.petra.util.Petra;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


import static io.cognitionbox.petra.util.Petra.rc;
import static io.cognitionbox.petra.util.Petra.rt;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class PSetUseageTest extends BaseExecutionModesTest {

  public PSetUseageTest(ExecMode execMode) {
    super(execMode);
  }

  static class TwoIntegerSets extends PSet<Integer> {
    private final PSet<Integer> numbers = new PSet<>();
  }
  @Test
  public void testSingleStep() {
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

  public static class SumTwoIntegerSets extends PEdge<TwoIntegerSets,Integer> {
    SumTwoIntegerSets(){
      pre(rc(TwoIntegerSets.class, a->true));
      func(x->{
        IToIntFunction<Integer> mapper = i->i.intValue();
        int sumA = x.stream().mapToInt(mapper).sum();
        int sumB = x.numbers.stream().mapToInt(mapper).sum();
        return sumA+sumB;
      });
      post(Petra.rt(Integer.class, a->true));
    }
  }

  public static class SingleStep extends PGraph<TwoIntegerSets,Integer> {
    SingleStep(){
      pre(rc(TwoIntegerSets.class, a->true));
      post(Petra.rt(Integer.class, a->true));
      step(new SumTwoIntegerSets());
    }
  }

}
