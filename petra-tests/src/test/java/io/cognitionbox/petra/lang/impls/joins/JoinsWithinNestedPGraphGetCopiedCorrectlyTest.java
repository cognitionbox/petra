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

import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import io.cognitionbox.petra.lang.annotations.Extract;
import io.cognitionbox.petra.config.ExecMode;

import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.PJoin;
import io.cognitionbox.petra.util.impl.PList;
import io.cognitionbox.petra.util.Petra;
import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;


import static io.cognitionbox.petra.util.Petra.readConsume;
import static io.cognitionbox.petra.util.Petra.returns;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class JoinsWithinNestedPGraphGetCopiedCorrectlyTest extends BaseExecutionModesTest {


  public JoinsWithinNestedPGraphGetCopiedCorrectlyTest(ExecMode execMode) {
    super(execMode);
  }

  @Extract
  public static class IntList extends PList<Integer> {
    IntList(){}
    IntList(Collection<Integer> e){
      super(e);
    }
  }

  public static class OutIntList extends PList<Integer> {
    OutIntList(){}
    OutIntList(Collection<Integer> e){
      super(e);
    }
  }

  @Test
  public void test() {
    //System.setProperty("mode","DIS");

    IntList list = new IntList();
    list.addAll(Arrays.asList(1,1,1,1,1,1));
    io.cognitionbox.petra.lang.PGraphComputer<IntList,OutIntList> lc = getGraphComputer();
    OutIntList result = lc.computeWithInput(new MultiStepsCreatedForMultipleMatchesWithNesting(), list);
    Assertions.assertThat(result).isEqualTo(new IntList(Arrays.asList(2,2,2,2,2,2)));
  }

  public static class MultiStepsCreatedForMultipleMatchesWithNesting extends PGraph<@Extract IntList,OutIntList> {
    {
      pre(readConsume(IntList.class, x->x.size()==6 && x.stream().mapToInt(i->i).sum()==6));
      post(returns(OutIntList.class, x->x.size()==6  && x.stream().mapToInt(i->i).sum()==12));
      step(new MultiplyOneByNesting());
      joinSome(new MultiStepsCreatedForMultipleMatchesWithNestingPureJoin());
    }
  }

  public static class MultiStepsCreatedForMultipleMatchesWithNestingPureJoin extends PJoin<Integer,OutIntList> {
   {
      pre(readConsume(Integer.class, x->x==2));
      func(x->new OutIntList(x));
      post(returns(OutIntList.class, x->x.size()==6 && x.stream().mapToInt(i->i).sum()==12));
    }
  }

  interface X1 {}
  interface X2 {}
  class X implements X1, X2 {}
  public static class Tmp extends PJoin<X,X> {
    {
      pre(readConsume(X1.class, a->true));
      func(x->{
        return x.get(0);
      });
      post(returns(X2.class, a->true));
    }
  }

  public static class MultiplyOneBy2 extends PEdge<Integer,IntList> {
    {
      pre(readConsume(Integer.class, x->x==1));
      post(returns(IntList.class, x->x.size()==2));
      func(x->{
        IntList list = new IntList();
        for (int i=0;i<2;i++){
          list.add(0);
        }
        return list;
      });
    }
  }

  public static class MultiplyOneByNesting extends PGraph<Integer,Integer> {
   {
      pre(readConsume(Integer.class, x->x==1));
      post(returns(Integer.class, x->x==2));
      step(new MultiplyOneBy2());
      joinSome(new MultiplyOneByNestingPureJoin());
    }
  }

  public static class MultiplyOneByNestingPureJoin extends PJoin<Integer,Integer> {
    {
      pre(readConsume(Integer.class, x->x==0));
      func(x->{
        return x.size();
      });
      post(returns(Integer.class, a->true));
    }
  }

}
