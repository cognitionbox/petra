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

import io.cognitionbox.petra.lang.annotations.Exclusive;
import io.cognitionbox.petra.lang.annotations.Feedback;
import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.core.IRollback;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import static io.cognitionbox.petra.util.Petra.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ExclusivesTest extends BaseExecutionModesTest {

  public ExclusivesTest(ExecMode execMode) {
    super(execMode);
  }

  @Exclusive public static class ExAtomicInteger extends AtomicInteger {}

  @Exclusive
  public static class A implements Serializable {
    @Override
    public String toString() {
      return "A{" +
              "integer=" + integer +
              '}';
    }

    @Exclusive
    final ExAtomicInteger integer = null;

    @Exclusive
    public ExAtomicInteger integer(){
      return integer;
    }
  }

  @Feedback
  public static class AtoA extends PEdge<A,A> implements IRollback<A> {
    {
      pre(readWrite(A.class, a->a.integer.get()==0 || a.integer.get()!=10));
      post(returns(A.class, a->a.integer.get()==10));
      func(a->{
        a.integer.incrementAndGet();
        return a;
      });
    }

    @Override
    public void capture(A input) {

    }

    @Override
    public void rollback(A input) {

    }
  }

  public static class g extends PGraph<A, A> {
    {
      pre(readWrite(A.class, a->a.integer.get()==0));
      post(returns(A.class, a->a.integer.get()==10));
      step(AtoA.class);
    }
  }


  @Test
  public void testExclusives() {

    io.cognitionbox.petra.lang.PGraphComputer<A, A> lc = getGraphComputer();
    A result = lc.computeWithInput(new g(), new A());
    assertThat(result.integer.get()).isEqualTo(10);
  }


// // @State
//  interface Y {}
//  interface Z {}
//
//  @State
//  interface X extends @State Y, Z{}
//
//  @Test
//  public void testStates() {
//   for (Type c : X.class.getGenericInterfaces()){
//     if (((Class<?>)c).isAnnotationPresent(State.class)){
//       System.out.println("state");
//     }
//   }
//  }




}
