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

import io.cognitionbox.petra.lang.PComputer;
import io.cognitionbox.petra.lang.annotations.Extract;
import io.cognitionbox.petra.config.ExecMode;

import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.util.impl.PList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.cognitionbox.petra.util.Petra.rt;


@RunWith(Parameterized.class)
public class BackReferencesDetectionTest extends BaseExecutionModesTest {
  // will help to ensure race conditions and deadlocks are impossible
  // this is currently achieved via the stack overflow exception when recursively deconstructing
  // if there is a back reference there will be a circular reference back to the parent when
  // deconstructing, there might be rw other cases missed.

  public BackReferencesDetectionTest(ExecMode execMode) {
    super(execMode);
  }

  @Extract
  static class BList extends PList<B> {}

  @Extract
  public static class A {
    int value = 0;


    private BList b;
    @Extract public BList blist(){
      return b;
    }
//@Extract private B b;

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

  @Extract
  public static class B {
    int value = 0;

    private A a;

    @Extract public A a(){
      return a;
    }

    @Override
    public String toString() {
      return "B{" +
              "value=" + value +
              '}';
    }

    public B(int value) {
      this.value = value;
    }
  }

  public static class AtoA extends PEdge<A> {
    {
      pc(A.class, x->true);
      func(a->new A(222));
      qc(A.class, x->true);
    }
  }

  public static class g extends PGraph<A> {
    {
      pc(A.class, x->true);
      qc(A.class, x->true);
      step(AtoA.class);
    }
  }


  @Test(expected = StackOverflowError.class)
  public void testBackReferencesDetection() {

    PComputer<A> lc = getGraphComputer();
    A result = lc.eval(new g(), new A(1));
  }

}
