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
package io.cognitionbox.petra.lang.impls.joins;

import io.cognitionbox.petra.lang.annotations.Extract;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import io.cognitionbox.petra.config.ExecMode;

import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.PJoin;
import io.cognitionbox.petra.util.impl.PList;
import io.cognitionbox.petra.util.Petra;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;


import static io.cognitionbox.petra.util.Petra.readConsume;
import static io.cognitionbox.petra.util.Petra.returns;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class JoinsSideEffectRollbackTest extends BaseExecutionModesTest {
  public JoinsSideEffectRollbackTest(ExecMode execMode) {
    super(execMode);
  }

  /*
    1 2 3 4 5 6 7

    joinSome, looks to match one state per predicate, collect these matches to a list, and transform
    readConsume 3, readConsume 5, readConsume 6 -> 3,5,6

    joinAll, filters all states into a list, transforms list
    readConsume even -> 2,4,6
   */

  @Extract
  static class IntList extends PList<AtomicInteger> {
    IntList(){}
    IntList(Collection<AtomicInteger> collection){
      super(collection);
    }
  }

  /*
   * testJoin 1 2 and 3 all transform the entire input
   */

  @Test
  public void testJoin1() {
    IntList list = new IntList();
    list.add(new AtomicInteger(1));
    list.add(new AtomicInteger(2));
    list.add(new AtomicInteger(3));
    list.add(new AtomicInteger(4));

    Object result = getGraphComputer().computeWithInput(new Join1JoinEdge(),list);
    assertThat(result).isNotEqualTo("ok");
  }

  public static class Join1JoinEdge extends PGraph<IntList,String> {
    {
      pre(readConsume(IntList.class, x->true));
      post(Petra.returns(String.class, x->x.equals("ok")));
      joinSome(new PureJoinOne());
    }
  }

  public static class PureJoinOne extends PJoin<AtomicInteger,String> {
    {
      pre(readConsume(AtomicInteger.class, x->true));
      func(x->{
        x.forEach(i->i.set(i.get()+1));
        if (true){
          throw new IllegalStateException();
        }
        return "ok";
      });
      post(Petra.returns(String.class, x->x.equals("ok")));
    }
  }
}
