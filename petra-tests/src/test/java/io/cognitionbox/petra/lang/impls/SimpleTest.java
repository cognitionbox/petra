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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.Serializable;

import static io.cognitionbox.petra.util.Petra.rt;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class SimpleTest extends BaseExecutionModesTest {

  public SimpleTest(ExecMode execMode) {
    super(execMode);
  }

  public static class B {}
  public static class A implements Serializable {
    int value = 0;

    transient B b = new B();

    @Override
    public String toString() {
      return "A{" +
              "value=" + value +
              '}';
    }

    public A(int value) {
      this.value = value;
    }
  }

  public static class AtoA extends PEdge<A> {
    {
      type(A.class);
      pre(a->a.value==1);
      func(a->{
        a.value = 222;
        return a;
      });
      post(a->a.value==222);
    }
  }

  public static class g extends PGraph<A> {
    {
      type(A.class);
      pre(a->a.value==1);
      post(a->a.value==222);
      step(AtoA.class);
    }
  }


  @Test
  public void testSimple() {

    getGraphComputer().getConfig().setDeadLockRecovery(true);
    getGraphComputer().getConfig().setDefensiveCopyAllInputsExceptForEffectedInputs(true);
    A res = (A) getGraphComputer().eval(new g(), new A(1));
    assertThat(res.value).isEqualTo(222);
  }

}
