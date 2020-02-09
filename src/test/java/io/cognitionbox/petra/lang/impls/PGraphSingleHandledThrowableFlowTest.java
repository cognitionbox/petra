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
package io.cognitionbox.petra.lang.impls;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.util.Petra;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.cognitionbox.petra.util.Petra.readConsume;
import static io.cognitionbox.petra.util.Petra.returns;
import static org.assertj.core.api.Assertions.assertThat;

@Ignore
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
      pre(readConsume(Integer.class, x->x==0));
      post(returns(Integer.class, x->x==3));
      step(new Nesting());
    }
  }

  public static class Nesting extends PGraph<Integer,Integer> {
    {
      pre(readConsume(Integer.class, x -> x == 0));
      post(returns(Integer.class, x->x==1 || x==3));
      step(new PlusOne());
    }
  }

  public static class MainLoopWithDirectStepHandledThrowable extends PGraph<Integer,Integer> {
    {
      pre(readConsume(Integer.class, x->x==0));
      post(returns(Integer.class, x->x==3));
      step(new NestingWithDirectStepHandledThrowable());
    }
  }

  public static class NestingWithDirectStepHandledThrowable extends PGraph<Integer,Integer> {
    {
      pre(readConsume(Integer.class, x -> x == 0));
      post(returns(Integer.class, x -> x == 3));
      step(new PlusOne());
    }
  }

  public static class PlusOne extends PEdge<Integer,Integer> {
   {
      pre(readConsume(Integer.class, x->x==0));
      post(returns(Integer.class, x->x==1 || x==3));
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
