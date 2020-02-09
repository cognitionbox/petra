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
