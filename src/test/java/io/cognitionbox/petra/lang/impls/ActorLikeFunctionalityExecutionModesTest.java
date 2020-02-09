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

import io.cognitionbox.petra.lang.*;
import io.cognitionbox.petra.lang.annotations.Extract;
import io.cognitionbox.petra.lang.annotations.Feedback;
import io.cognitionbox.petra.config.ExecMode;

import io.cognitionbox.petra.util.impl.PSet;
import io.cognitionbox.petra.core.IRollback;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static io.cognitionbox.petra.util.Petra.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ActorLikeFunctionalityExecutionModesTest extends BaseExecutionModesTest {

  public ActorLikeFunctionalityExecutionModesTest(ExecMode execMode) {
    super(execMode);
  }

  @Extract
  private static class InSet extends PSet<A> {
    InSet(Collection<A> collection){
      super(collection);
    }
  }

  private static class OutSet extends PSet<AisEqualTo11_OR_64> {
    OutSet(){}
    OutSet(Collection<AisEqualTo11_OR_64> collection){
      super(collection);
    }
  }

  public static class MainJoin extends PJoin2<AisEqualTo11, AisEqualTo64,OutSet> {
    {
      preA(readConsume(AisEqualTo11.class, a->a.i()==11));
      preB(readConsume(AisEqualTo64.class, a->a.i()==64));
      func((a, b)->{
        AisEqualTo11_OR_64 one = a.get(0);
        AisEqualTo11_OR_64 two = b.get(0);
        OutSet out = new OutSet();
        out.add(one);
        out.add(two);
        return out;
      });
      post(returns(OutSet.class, x->{
        return new OutSet(x).equals(new OutSet(Arrays.asList(new A(11),new A(64))));
      }));
    }
  }

  @Feedback
  public static class OppA extends PEdge<A,A> implements IRollback<A> {
    {
      pre(readWrite(AisEvenAndNotEqualTo64.class, a->a.i()%2==0 && a.i()!=64));
      func(x->{
        x.multiplyIby2();
        return x;
      });
      post(returns(AisEqualTo64.class, a->a.i()==64)); // lets output pass through
    }

    @Override
    public void capture(A input) {

    }

    @Override
    public void rollback(A input) {

    }
  }

  @Feedback
  public static class OppB extends PEdge<A,A> implements IRollback<A>{
    {
      pre(readWrite(AisOddAndNotEqualTo11.class, a->a.i()%2!=0 && a.i()!=11));
      func(x->{
        x.incrementIby2();
        return x;
      }); // 1, 3, 5, 7, 9
      post(returns(AisEqualTo11.class, a->a.i()==11)); // lets output pass through
    }

    @Override
    public void capture(A input) {

    }

    @Override
    public void rollback(A input) {

    }
  }

  public static class Main extends PGraph<InSet,OutSet> {
    {
      pre(readConsume(InSet.class, x->true));
      step(new OppA());
      step(new OppB());
//      step(readConsume(A.class),x->{
//        state.out.println(x);
//        return x;
//      },readConsume(A.class));
      joinSome(new MainJoin());
      post(returns(OutSet.class, x->{
        return x.equals(new OutSet(Arrays.asList(new A(11),new A(64))));
      }));
    }
  }

  @Test
  public void testActorLikeFunctionality(){

    io.cognitionbox.petra.lang.PGraphComputer<InSet,OutSet> lc = getGraphComputer();
    A x = new A(1);
    A y = new A(2);
      InSet aSet = new InSet(Arrays.asList(x,y));



    Set result =  lc.computeWithInput(new Main(), aSet);
    System.out.println(
        result
    );

    assertThat(result).isEqualTo(new HashSet(Arrays.asList(new A(11),new A(64))));
  }

  interface GetI {
    int i();
  }
  interface SetI {
    void i(int i);
  }
  interface AisEvenAndNotEqualTo64 extends GetI, SetI {
    default void multiplyIby2(){
      i(i()*2);
    }
  }

  interface AisOddAndNotEqualTo11 extends GetI, SetI {
    default void incrementIby2(){
      i(i()+2);
    }
  }

  // ORs are done like this
  interface AisEqualTo11 extends GetI, AisEqualTo11_OR_64 {
  }

  interface AisEqualTo64 extends GetI, AisEqualTo11_OR_64 {
  }

  interface AisEqualTo11_OR_64 {}



  public static class A implements Serializable, AisEvenAndNotEqualTo64, AisOddAndNotEqualTo11, AisEqualTo11, AisEqualTo64 {
    // use always use if want to run across all modes
    // use list, queue, set, map, for collections
    Ref<Integer> i;
    public A(int initial){
      i = ref(initial);
    }

    @Override
    public String toString() {
      return "A{" +
          "i=" + i +
          '}';
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      A a = (A) o;

      return i.equals(a.i);
    }

    @Override
    public int hashCode() {
      return i.hashCode();
    }

    @Override
    public int i() {
      return i.get();
    }

    @Override
    public void i(int i) {
      this.i.set(i);
    }
  }

}