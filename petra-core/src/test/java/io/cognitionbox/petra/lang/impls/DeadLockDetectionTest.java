/**
 * Copyright 2016-2020 Aran Hakki
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cognitionbox.petra.lang.impls;


import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.exceptions.GraphException;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.RGraphComputer;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import io.cognitionbox.petra.lang.config.IPetraTestConfig;

import java.io.Serializable;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
@Ignore
public class DeadLockDetectionTest extends BaseExecutionModesTest {

    public DeadLockDetectionTest(ExecMode execMode) {
        super(execMode);
    }

    public static class B implements Serializable {
    }

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
            pre(a -> a.value == 1);
            func(a -> {
                ThreadDemo1 T1 = new ThreadDemo1();
                ThreadDemo2 T2 = new ThreadDemo2();
                T1.start();
                T2.start();

                try {
                    T1.join();
                    T2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                a.value = 222;
            });
            post(a -> a.value == 222);
        }
    }

    //@Effect
    public static class g extends PGraph<A> {
        {
            type(A.class);
            pre(a -> a.value == 1);
            post(a -> a.value == 222);
            step(AtoA.class);
        }
    }


    @Test
    public void testDeadDetection() {

        getGraphComputer().getConfig().setDeadLockRecovery(true);
        getGraphComputer().getConfig().setDefensiveCopyAllInputsExceptForEffectedInputs(true);
        ((IPetraTestConfig) RGraphComputer.getConfig()).allowExceptionsPassthrough();
        Object res = getGraphComputer().eval(new g(), new A(1));
        assertThat(res).isInstanceOf(GraphException.class);
        TimeoutException to = (TimeoutException) ((GraphException) res).getCauses().get(0).getCause();
    }

    public static Object Lock1 = new Object();
    public static Object Lock2 = new Object();

    private static class ThreadDemo1 extends Thread {
        public void run() {
            synchronized (Lock1) {
                System.out.println("Thread 1: Holding lock 1...");

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
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

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
                System.out.println("Thread 2: Waiting for lock 1...");

                synchronized (Lock1) {
                    System.out.println("Thread 2: Holding lock 1 & 2...");
                }
            }
        }
    }

}
