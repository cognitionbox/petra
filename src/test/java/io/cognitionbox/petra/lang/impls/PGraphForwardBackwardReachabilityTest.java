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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.Serializable;


import static io.cognitionbox.petra.util.Petra.readConsume;
import static io.cognitionbox.petra.util.Petra.returns;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class PGraphForwardBackwardReachabilityTest extends BaseExecutionModesTest {

  public PGraphForwardBackwardReachabilityTest(ExecMode execMode) {
    super(execMode);
  }

  public static class A implements ReadA, AisOne, Serializable {
    int value = 0;

    @Override
    public String toString() {
      return "A{" +
              "value=" + value +
              '}';
    }

    public A(int value) {
      this.value = value;
    }

    @Override
    public int value() {
      return value;
    }
  }

  public static class B implements ReadB, BisEven, Serializable{
    int value = 0;

    @Override
    public String toString() {
      return "B{" +
              "value=" + value +
              '}';
    }

    public B(int value) {
      this.value = value;
    }

    @Override
    public int value() {
      return value;
    }
  }

  public static interface ReadA {
    int value();
  }
  public static interface ReadB {
    int value();
  }

  public static interface AisOne extends ReadA{
    default boolean matches(){
      return this.value()==1;
    }
  }

  public static interface BisEven extends ReadB {
    default boolean matches(){
      return this.value()%2==0;
    }
  }

  public static class AtoB extends PEdge<AisOne,BisEven> {
    {
      pre(readConsume(AisOne.class, x->x.matches()));
      func(a->new B(222));
      post(Petra.returns(BisEven.class, x->x.matches()));
    }
  }


  public static class g extends PGraph<AisOne, BisEven> {
    {
      pre(readConsume(AisOne.class, x->x.matches()));
      post(Petra.returns(BisEven.class, x->x.matches()));
      step(AtoB.class);
    }
  }


  @Test
  public void testForwardBackwardReasoning() {

    //PetraTestConfig.setBackwardReasoning(true);
    io.cognitionbox.petra.lang.PGraphComputer<AisOne, BisEven> lc = getGraphComputer();
    BisEven result = lc.computeWithInput(new g(), new A(1));
  }

}
