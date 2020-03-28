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
package io.cognitionbox.petra.lang.impls;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.util.Petra;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.cognitionbox.petra.util.Petra.rc;
import static io.cognitionbox.petra.util.Petra.rt;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class PGraphSingleHandledThrowableFlowTest extends BaseExecutionModesTest {


  public PGraphSingleHandledThrowableFlowTest(ExecMode execMode) {
    super(execMode);
  }

  @Test
  public void testThrowableFlow() {

    Object result = getGraphComputer().computeWithInput(new MainLoop(),0);
    assertThat(result).isInstanceOf(Integer.class);
    assertThat(result).isEqualTo(3);
  }

  @Test
  public void testThrowableFlowWithDirectStepHandledThrowable() {

    Object result = getGraphComputer().computeWithInput(new MainLoopWithDirectStepHandledThrowable(),0);
    assertThat(result).isInstanceOf(Integer.class);
    assertThat(result).isEqualTo(3);
  }

  public static class MainLoop extends PGraph<Integer,Integer> {
    {
      pre(rc(Integer.class, x->x==0));
      post(Petra.rt(Integer.class, x->x==3));
      step(new Nesting());
    }
  }

  public static class Nesting extends PGraph<Integer,Integer> {
    {
      pre(rc(Integer.class, x -> x == 0));
      post(Petra.rt(Integer.class, x->x==1 || x==3));
      step(new PlusOne());
    }
  }

  public static class MainLoopWithDirectStepHandledThrowable extends PGraph<Integer,Integer> {
    {
      pre(rc(Integer.class, x->x==0));
      post(Petra.rt(Integer.class, x->x==3));
      step(new NestingWithDirectStepHandledThrowable());
    }
  }

  public static class NestingWithDirectStepHandledThrowable extends PGraph<Integer,Integer> {
    {
      pre(rc(Integer.class, x -> x == 0));
      post(Petra.rt(Integer.class, x -> x == 3));
      step(new PlusOne());
    }
  }

  public static class PlusOne extends PEdge<Integer,Integer> {
   {
      pre(rc(Integer.class, x->x==0));
      post(Petra.rt(Integer.class, x->x==1 || x==3));
      func(x->{
        try {
          int y = 1/0;
        } catch (ArithmeticException e){
          return 3;
        }
        return null;
      });
    }
  }
}
