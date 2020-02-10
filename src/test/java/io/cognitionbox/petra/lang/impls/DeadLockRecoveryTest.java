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
import io.cognitionbox.petra.lang.RGraphComputer;
import io.cognitionbox.petra.lang.config.PetraTestConfig;
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

//@Ignore
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
      pre(readConsume(A.class, a->a.value==1));
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
      post(Petra.returns(A.class, a->a.value==222));
    }
  }

  public static class g extends PGraph<A,A> {
    {
      pre(readConsume(A.class, a->a.value==1));
      post(Petra.returns(A.class, a->a.value==222));
      step(AtoA.class);
    }
  }


  @Test
  public void testDeadLockRecovery() {

    getGraphComputer().getConfig().setDeadLockRecovery(true);
    ((PetraTestConfig) getGraphComputer().getConfig()).disableExceptionsPassthrough();
    io.cognitionbox.petra.lang.PGraphComputer<A, A> lc = getGraphComputer();
    g g = new g();
    A result = (A) getGraphComputer().computeWithInput(g, new A(1));
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
