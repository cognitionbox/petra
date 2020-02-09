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
package io.cognitionbox.petra.lang.impls.joins;

import io.cognitionbox.petra.lang.*;
import io.cognitionbox.petra.lang.Void;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import io.cognitionbox.petra.lang.annotations.Extract;
import io.cognitionbox.petra.config.ExecMode;

import io.cognitionbox.petra.util.impl.PList;
import io.cognitionbox.petra.util.impl.PSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import static io.cognitionbox.petra.util.Petra.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class JoinsTest extends BaseExecutionModesTest {
  public JoinsTest(ExecMode execMode) {
    super(execMode);
  }

  /*
    1 2 3 4 5 6 7

    joinSome, looks to match one state per predicate, collect these matches to a list, and transform
    readConsume 3, readConsume 5, readConsume 6 -> 3,5,6

    joinAll, filters all states into a list, transforms list
    readConsume even -> 2,4,6
   */

  private static IntSet IntSet_1234(){
   return new IntSet(Arrays.asList(1, 2, 3, 4));
  }

  static class IntList extends PList<Integer> {
    IntList(){}
    IntList(Collection<Integer> collection){
      super(collection);
    }
  }

  @Extract
  static class IntSet extends PSet<Integer> {
    IntSet(){}
    IntSet(Collection<Integer> collection){
      super(collection);
    }
  }

  static class OutIntSet extends PSet<Integer> {
    OutIntSet(){}
    OutIntSet(Collection<Integer> collection){
      super(collection);
    }
  }

  @Test
  public void testJoinUnity() {
    Object result = getGraphComputer().computeWithInput(new JoinUnity(),IntSet_1234());
    assertThat(result)
        .isEqualTo(new HashSet(Arrays.asList(1, 2, 3, 4)));
  }

  /*
   * testJoin 1 2 and 3 all transform the entire input
   */

  @Test
  public void testJoin1() {
    Object result = getGraphComputer().computeWithInput(new Join1JoinEdge(),IntSet_1234());
    assertThat(result)
            .isEqualTo(new HashSet(Arrays.asList(1,2,3,4)));
  }

  @Test
  public void testJoin2() {
    Object result = getGraphComputer().computeWithInput(new Join2JoinEdge(),IntSet_1234());
    assertThat(result)
            .isEqualTo(new HashSet(Arrays.asList(2,4)));
  }

  @Test
  public void testJoin3() {
    Object result = getGraphComputer().computeWithInput(new Join3JoinEdge(),IntSet_1234());
    assertThat(result)
            .isEqualTo(new HashSet(Arrays.asList(1,2,3,4)));
  }

  @Test
  public void testSuccessfulMultiJoin() {
    Object result = getGraphComputer().computeWithInput(new SuccessfulDependantMultiJoin(),IntSet_1234());
    assertThat(result)
            .isEqualTo(new IntList(Arrays.asList(7)));
  }

  @Test
  public void testSuccessfulMultiJoinWithConsumption() {
    Object result = getGraphComputer().computeWithInput(new SuccessfulDependantMultiJoinWithConsumption(),IntSet_1234());
    assertThat(result)
            .isEqualTo(new HashSet(Arrays.asList(2,4)));
  }

  public static class JoinUnity extends PGraph<@Extract IntSet,OutIntSet> {
    JoinUnity(){
      pre(readConsume(IntSet.class, s->s.equals(IntSet_1234())));
      post(returns(OutIntSet.class, x->x.size()==4));
      joinSome(new JoinUnityPureJoin());
    }
  }

  public static class JoinUnityPureJoin extends PJoin<Integer,OutIntSet> {
    JoinUnityPureJoin(){
      pre(readConsume(Integer.class, x->true));
      func(x->new OutIntSet(x));
      post(returns(OutIntSet.class, x->x.size()==4));
    }
  }

  public static class Join1JoinEdge extends PGraph<@Extract IntSet,OutIntSet> {
    Join1JoinEdge(){
      pre(readConsume(IntSet.class, s->s.equals(IntSet_1234())));
      post(returns(OutIntSet.class, x->x.size()==4));
      joinSome(new PureJoinOne());
    }
  }

  public static class PureJoinOne extends PJoin<Integer,OutIntSet> {
    public PureJoinOne(){
      pre(readConsume(Integer.class, x->true));
      func(x->new OutIntSet(x));
      post(returns(OutIntSet.class, x->x.size()==4));
    }
  }

  public static class Join2JoinEdge extends PGraph<@Extract IntSet,OutIntSet> {
    Join2JoinEdge(){
      pre(readConsume(IntSet.class, s->s.equals(IntSet_1234())));
      post(returns(OutIntSet.class, x->x.size()==2));
      joinSome(new PureJoinTwo());
    }
  }

  public static class PureJoinTwo extends PJoin2<Integer,Integer,OutIntSet> {
    public PureJoinTwo(){
      preA(readConsume(Integer.class, x->x%2==0));
      preB(readConsume(Integer.class, x->x%2!=0));
      func((a, b)->new OutIntSet(a));
      post(returns(OutIntSet.class, x->x.size()==2));
    }
  }

  public static class Join3JoinEdge extends PGraph<@Extract IntSet,OutIntSet> {
    Join3JoinEdge(){
      pre(readConsume(IntSet.class, s->s.equals(IntSet_1234())));
      post(returns(OutIntSet.class, x->x.size()==4));
      joinSome(new PureJoinThree());
    }
  }

  public static class PureJoinThree extends PJoin3<Integer,Integer,Integer,OutIntSet> {
    public PureJoinThree(){
      preA(readConsume(Integer.class, x->x%2==0));
      preB(readConsume(Integer.class, x->x==1));
      preC(readConsume(Integer.class, x->x==3));
      func((a, b, c)->{
        OutIntSet set = new OutIntSet();
        set.addAll(a);
        set.addAll(b);
        set.addAll(c);
        return set;
      });
      post(returns(OutIntSet.class, x->x.size()==4));
    }
  }

  public static class SuccessfulDependantMultiJoin extends PGraph<@Extract IntSet,IntList> {
    SuccessfulDependantMultiJoin(){
      pre(readConsume(IntSet.class, s->s.equals(IntSet_1234())));
      post(returns(IntList.class, x->x.size()==1));
      joinSome(new SuccessfulDependantMultiPureJoin1());
      joinSome(new SuccessfulDependantMultiPureJoin2());
    }
  }

  public static class SuccessfulDependantMultiPureJoin1 extends PJoin<Integer,Integer> {
    SuccessfulDependantMultiPureJoin1(){
      pre(readConsume(Integer.class, x->x!=7));
      func(x->7);
      post(returns(Integer.class, x->x==7));
    }
  }

  public static class SuccessfulDependantMultiPureJoin2 extends PJoin<Integer,IntList> {
    SuccessfulDependantMultiPureJoin2(){
      pre(readConsume(Integer.class, x->x==7));
      func(x->new IntList(x));
      post(returns(IntList.class, x->x.size()==1));
    }
  }

  public static class SuccessfulDependantMultiJoinWithConsumption extends PGraph<@Extract IntSet,OutIntSet> {
    SuccessfulDependantMultiJoinWithConsumption(){
      pre(readConsume(IntSet.class, s->s.equals(IntSet_1234())));
      post(returns(OutIntSet.class, x->x.size()==2));
      joinSome(new SuccessfulDependantMultiJoinWithConsumptionPureJoin1());
      joinSome(new SuccessfulDependantMultiJoinWithConsumptionPureJoin2());
    }
  }

  public static class SuccessfulDependantMultiJoinWithConsumptionPureJoin1 extends PJoin<Integer,OutIntSet> {
    SuccessfulDependantMultiJoinWithConsumptionPureJoin1(){
      pre(readConsume(Integer.class, x->x%2==0));
      func(x->new OutIntSet(x));
      post(returns(OutIntSet.class, x->x.size()==2));
    }
  }

  public static class SuccessfulDependantMultiJoinWithConsumptionPureJoin2 extends PJoin<Integer, Void> {
    SuccessfulDependantMultiJoinWithConsumptionPureJoin2(){
      pre(readConsume(Integer.class, x->x%2!=0));
      func(x->null);
      postVoid();
    }
  }
}
