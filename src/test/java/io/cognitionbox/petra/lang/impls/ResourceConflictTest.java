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

import io.cognitionbox.petra.lang.annotations.Extract;
import io.cognitionbox.petra.lang.annotations.SharedResource;
import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.util.impl.PList;
import io.cognitionbox.petra.util.Petra;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.cognitionbox.petra.util.Petra.readConsume;
import static io.cognitionbox.petra.util.Petra.returns;


@RunWith(Parameterized.class)
public class ResourceConflictTest extends BaseExecutionModesTest {

  public ResourceConflictTest(ExecMode execMode) {
    super(execMode);
  }

  @Extract
  static class BList extends PList<B> {}

  @Extract
  public static class A {
    int value = 0;

    @Extract
    private BList b;

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

  @SharedResource
  public static class B {
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
  }

  public static class AtoA extends PEdge<A,A> {
    {
      pre(readConsume(A.class, x->true));
      func(a->new A(222));
      post(Petra.returns(A.class, x->true));
    }
  }

  public static class g extends PGraph<A,A> {
    {
      pre(readConsume(A.class, x->true));
      post(Petra.returns(A.class, x->true));
      step(AtoA.class);
    }
  }


  @Test(expected = AssertionError.class)
  public void testResourceConflict() {

    io.cognitionbox.petra.lang.PGraphComputer<A, A> lc = getGraphComputer();
    A result = lc.computeWithInput(new g(), new A(1));
  }

}
