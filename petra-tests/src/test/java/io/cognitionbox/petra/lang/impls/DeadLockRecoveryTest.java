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

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.lang.RGraphComputer;
import io.cognitionbox.petra.lang.config.IPetraTestConfig;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.util.Petra;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.Serializable;


import static io.cognitionbox.petra.util.Petra.rc;
import static io.cognitionbox.petra.util.Petra.rt;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class DeadLockRecoveryTest extends BaseExecutionModesTest {

  public DeadLockRecoveryTest(ExecMode execMode) {
    super(execMode);
  }

  public static class A implements Serializable {
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
  }

  public static class AtoA extends PEdge<A,A> {
    {
      pre(rc(A.class, a->a.value==1));
      func(a->{
        if (Math.random()>0.2){
          ThreadDemo1 T1 = new ThreadDemo1();
          ThreadDemo2 T2 = new ThreadDemo2();
          T1.start();
          T2.start();
          try {
            T1.join();
            T2.join();
          } catch (InterruptedException e) {
            throw new IllegalStateException(e);
          }
        }
        return new A(222);
      });
      post(Petra.rt(A.class, a->a.value==222));
    }
  }

  public static class g extends PGraph<A,A> {
    {
      pre(rc(A.class, a->a.value==1));
      post(Petra.rt(A.class, a->a.value==222));
      step(AtoA.class);
    }
  }


  @Test
  public void testDeadLockRecovery() {

    RGraphComputer.getConfig().setDeadLockRecovery(true);
    ((IPetraTestConfig) RGraphComputer.getConfig()).disableExceptionsPassthrough();
    io.cognitionbox.petra.lang.PGraphComputer<A, A> lc = getGraphComputer();
    g g = new g();
    A result = lc.computeWithInput(g, new A(1));
  }

  public static Object Lock1 = new Object();
  public static Object Lock2 = new Object();

  private static class ThreadDemo1 extends Thread {
    public void run() {
      synchronized (Lock1) {
        System.out.println("Thread 1: Holding lock 1...");

        try { Thread.sleep(10); }
        catch (InterruptedException e) {}
        System.out.println("Thread 1: Waiting for lock 2...");

        synchronized (Lock2) {
          System.out.println("Thread 1: Holding lock 1 & 2...");
        }
      }
    }
  }
  private static class ThreadDemo2 extends Thread {
    public void run() {
      synchronized (Lock2) {
        System.out.println("Thread 2: Holding lock 2...");

        try { Thread.sleep(10); }
        catch (InterruptedException e) {}
        System.out.println("Thread 2: Waiting for lock 1...");

        synchronized (Lock1) {
          System.out.println("Thread 2: Holding lock 1 & 2...");
        }
      }
    }
  }

}
