/*
 ********************************************************************************************************
 * Copyright © 2016-2020 Cognition Box Ltd. - All Rights Reserved
 *
 * This file is part of the "Petra" system. "Petra" is owned by:
 *
 * Cognition Box Ltd. (10194162)
 * 9 Grovelands Road,
 * Palmers Green,
 * London, N13 4RJ
 * England.
 *
 * "Petra" is Proprietary and Confidential.
 * Unauthorized copying of "Petra" files, via any medium is strictly prohibited.
 *
 * "Petra" can not be copied and/or distributed without the express
 * permission of Cognition Box Ltd.
 *
 * "Petra" includes trade secrets of Cognition Box Ltd.
 * In order to protect "Petra", You shall not decompile, reverse engineer, decrypt,
 * extract or disassemble "Petra" or otherwise reduce or attempt to reduce any software
 * in "Petra" to source code form. You shall ensure, both during and
 * (if you still have possession of "Petra") after the performance of this Agreement,
 * that (i) persons who are not bound by a confidentiality agreement consistent with this Agreement
 * shall not have access to "Petra" and (ii) persons who are so bound are put on written notice that
 * "Petra" contains trade secrets, owned by and proprietary to Cognition Box Ltd.
 *
 * "Petra" is written by Aran Hakki <aran@cognitionbox.io>
 ********************************************************************************************************
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

@Ignore
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
