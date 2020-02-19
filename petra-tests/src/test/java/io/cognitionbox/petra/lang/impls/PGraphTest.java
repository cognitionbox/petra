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

import io.cognitionbox.petra.core.impl.*;
import io.cognitionbox.petra.lang.*;
import io.cognitionbox.petra.lang.annotations.Extract;
import io.cognitionbox.petra.config.ExecMode;

import io.cognitionbox.petra.util.impl.PList;
import io.cognitionbox.petra.util.impl.PSet;
import io.cognitionbox.petra.core.IRollback;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import static io.cognitionbox.petra.util.Petra.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class PGraphTest extends BaseExecutionModesTest {

  public PGraphTest(ExecMode execMode) {
    super(execMode);
  }

  @Test
  public void testSideEffect() {

    A result = (A) getGraphComputer().computeWithInput(new AtoAPPGraph(),new A());
    A a = new A();
    a.value.set(1);
    assertThat(result.value).isEqualTo(a.value);
  }

  @Test
  public void testSingleStep() {

    Object result = getGraphComputer().computeWithInput(new SingleStep(),1);
    assertThat(result).isEqualTo(new IntegerBox(1));
  }

  public static class SingleStep extends PGraph<Integer,IntegerBox> {
    SingleStep(){
      pre(readConsume(Integer.class, x->true));
      post(returns(IntegerBox.class, x->true));
      step(new BoxIntegers());
    };
  }

  public static class BoxIntegers extends PEdge<Integer,IntegerBox> {
    BoxIntegers(){
      pre(readConsume(Integer.class, x->true));
      post(returns(IntegerBox.class, x->true));
      func(x->{
        return new IntegerBox(x);
      });
    }
  }

  @Extract
  static class IntList extends PList<Integer> {
    IntList(){}
    IntList(Collection<Integer> e){
      super(e);
    }
  }

  static class OutIntList extends PList<Integer> {
    OutIntList(){}
    OutIntList(Collection<Integer> e){
      super(e);
    }
  }

  @Test
  public void testMultiStepsCreatedForMultipleMatchesWithNesting() {


    IntList list = new IntList();
    list.addAll(Arrays.asList(0,0,0,0,0,0));
    PGraphComputer<IntList,OutIntList> lc = getGraphComputer();
    OutIntList result = lc.computeWithInput(new MultiStepsCreatedForMultipleMatchesWithNesting(), list);
    Assertions.assertThat(result).isEqualTo(new OutIntList(Arrays.asList(1,1,1,1,1,1)));
  }

  public static class MultiStepsCreatedForMultipleMatchesWithNesting extends PGraph<@Extract IntList,OutIntList> {
    MultiStepsCreatedForMultipleMatchesWithNesting(){
      pre(readConsume(IntList.class, x->x.size()==6 && x.stream().mapToInt(i->i).sum()==0));
      post(returns(OutIntList.class, x->x.size()==6  && x.stream().mapToInt(i->i).sum()==6));
      step(new Nesting());
      joinSome(new MultiStepsCreatedForMultipleMatchesWithNestingPureJoin());
    }
  }

  public static class MultiStepsCreatedForMultipleMatchesWithNestingPureJoin extends PJoin<Integer,OutIntList> {
    MultiStepsCreatedForMultipleMatchesWithNestingPureJoin(){
      pre(readConsume(Integer.class, x->x==1));
      func(x->new OutIntList(x));
      post(returns(OutIntList.class, x->x.size()==6 && x.stream().mapToInt(i->i).sum()==6));
    }
  }

  @Test
  public void testMultiStepsCreatedForMultipleMatches() {


    IntList list = new IntList();
    list.addAll(Arrays.asList(0,0,0,0,0,0));
    PGraphComputer<IntList,OutIntList> lc = getGraphComputer();
    OutIntList result = lc.computeWithInput(new MultiStepsCreatedForMultipleMatches(), list);
    Assertions.assertThat(result).isEqualTo(new IntList(Arrays.asList(1,1,1,1,1,1)));
  }

  public static class MultiStepsCreatedForMultipleMatches extends PGraph<@Extract IntList,OutIntList> {
    MultiStepsCreatedForMultipleMatches(){
      pre(readConsume(IntList.class, x->x.size()==6 && x.stream().mapToInt(i->i).sum()==0));
      post(returns(OutIntList.class, x->x.size()==6  && x.stream().mapToInt(i->i).sum()==6));
      step(new PlusOne());
      joinSome(new MultiStepsCreatedForMultipleMatchesPureJoin());
    }
  }

  public static class MultiStepsCreatedForMultipleMatchesPureJoin extends PJoin<Integer,OutIntList> {
    MultiStepsCreatedForMultipleMatchesPureJoin(){
      pre(readConsume(Integer.class, x->x==1));
      func(x->new OutIntList(x));
      post(returns(OutIntList.class, x->x.size()==6 && x.stream().mapToInt(i->i).sum()==6));
    }
  }

  @Extract
  static class MixedSet extends PSet {}

  @Test
  public void testMultiStep() {

    getGraphComputer().getConfig().setIsReachabilityChecksEnabled(false);
    MixedSet mixedSet = new MixedSet();
    mixedSet.addAll(Arrays.asList(0,"A"));
    PGraphComputer<MixedSet, PSet> lc = getGraphComputer();
    PSet result = lc.computeWithInput(new g(), mixedSet);
    assertThat(result).isEqualTo(new PSet(Arrays.asList(1,"B")));
  }


  public static class g extends PGraph<MixedSet, PSet> {
    g(){
      pre(readConsume(MixedSet.class, x->true));
      post(returns(PSet.class, x->x.equals(new PSet(Arrays.asList(1,"B")))));
      step(new PlusOne());
      step(AtoB.class);
      joinSome(new gPureJoin());
    }
  }

  public static class gPureJoin extends PJoin2<Integer,String,PSet> {
    gPureJoin(){
      preA(readConsume(Integer.class, x->x==1));
      preB(readConsume(String.class, s->s.equals("B")));
      func((x, y)->{
        PSet set = new PSet();
        set.add(x.get(0));
        set.add(y.get(0));
        return set;
      });
      post(returns(PSet.class, x->new PSet(x).equals(new PSet(Arrays.asList(1,"B")))));
    }
  }


  public static class PlusOne extends PEdge<Integer,Integer> {
    PlusOne(){
      pre(readConsume(Integer.class, x->x==0));
      post(returns(Integer.class, x->x==1));
      func(x->x+1);
    }
  }


  public static class AtoB extends PEdge<String,String> {
    {
      pre(readConsume(String.class, x->x.equals("A")));
      post(returns(String.class, x->x.equals("B")));
      func(x->"B");
    }
  }

  public static class IntegerBox extends Box<Integer> {
    public IntegerBox(Integer boxed) {
      super(boxed);
    }
  }

  @Test
  public void testNesting() {

    Object result = getGraphComputer().computeWithInput(new MainLoop(),0);
    assertThat(result).isEqualTo(1);
  }


  public static class MainLoop extends PGraph<Integer,Integer> {
    MainLoop(){
      pre(readConsume(Integer.class, x->x==0));
      post(returns(Integer.class, x->x==1));
      step(new Nesting());
    }
  }


  public static class Nesting extends PGraph<Integer,Integer> {
    Nesting(){
      pre(readConsume(Integer.class, x->x==0));
      post(returns(Integer.class, x->x==1));
      step(new PlusOne());
    }
  }

  @Test(expected = AssertionError.class)
  public void testOverlapCheck() {

    Object result = getGraphComputer().computeWithInput(new OverlapCheck(),2);
    assertThat(result).isNotEqualTo(3);
  }

  public static class PlusOneWhenXis2 extends PEdge<Integer,Integer> {
    PlusOneWhenXis2(){
      pre(readConsume(Integer.class, x->x==2));
      post(returns(Integer.class, x->true));
      func(x->x+1);
    }
  }

  public static class PlusOneWhenXisEven extends PEdge<Integer,Integer> {
    PlusOneWhenXisEven(){
      pre(readConsume(Integer.class, x->x==2));
      post(returns(Integer.class, x->true));
      func(x->x+1);
    }
  }

  public static class OverlapCheck extends PGraph<Integer,Integer> {
    {
      pre(readConsume(Integer.class, x->x==2));
      post(returns(Integer.class, x->x==3));
      step(new PlusOneWhenXis2());
      step(new PlusOneWhenXisEven());
    }
  }

  public static class A implements Serializable {
    Ref<Integer> value = ref(0);

    @Override
    public String toString() {
      return "A{" +
              "value=" + value +
              '}';
    }
  }

  public static class AtoA extends PEdge<A,A> implements IRollback<A>{
    {
      pre(readWrite(A.class, a->a.value.get().equals(0)));
      post(returns(A.class, a->a.value.get().equals(1)));
      func(x->{
        x.value.set(x.value.get()+1);
        return x;
      });
    }

    @Override
    public void capture(A input) {

    }

    @Override
    public void rollback(A input) {

    }
  }

  //@Effect
  public static class AtoAPPGraph extends PGraph<A,A> {
    {
      pre(readConsume(A.class, a->a.value.get().equals(0)));
      post(returns(A.class, a->a.value.get().equals(1)));
      step(new AtoA());
    }
  }

  public static class Y implements Serializable {
    private String name;

    @Override
    public String toString() {
      return "Y{" +
              "name='" + name + '\'' +
              '}';
    }

    public Y(String name) {
      this.name = name;
    }
  }

  //interface YRef extends Ref<Y>{}

  public static class Z implements Serializable{
    final Ref<Y> captured = ref();
    // in DIS mode all fields of classes that implement PState must be final
    // create check for this
    final Ref<Y> y = ref(new Y("hello"));

    // in SEQ/PAR in process modes, field can be modified but need to make sure changes are visible on all threads.
    // a synchronized block has been added to PEdge for this purpose

    @Override
    public String toString() {
      return "Z{" +
              "y=" + y +
              '}';
    }
  }

  @Extract
  public static class X implements Serializable{
    @Extract // cannot extract Refs
    Ref<Y> y = ref(new Y("hello"));

    @Override
    public String toString() {
      return "X{" +
              "y=" + y +
              '}';
    }
  }


  @Test
  public void testRefGraph() {

    Z result = (Z) ((PGraphComputer<Z,Z>)getGraphComputer()).computeWithInput(new RefPGraph(),new Z());
  }

  // PGraph<Ref<X>, Ref<Y>>, need to fix check to stop diff types for side affects

  public static class RefPGraph extends PGraph<Z, Z> {
    {
      pre(readConsume(Z.class, z->z.y.get().name.equals("hello")));
      post(returns(Z.class, z->z.y.get().name.equals("goodbye")));
      step(new XPEdge());
    }
  }

  public static class XPEdge extends PEdge<Z,Z> implements IRollback<Z> {
     {
      pre(readWrite(Z.class, z->z.y.get().name.equals("hello")));
      post(returns(Z.class, z->z.y.get().name.equals("goodbye")));
      func(z->{
        z.y.set(new Y("goodbye"));
        return z;
      });
    }

    @Override
    public void capture(Z input) {
      input.captured.set(input.y.get());
    }

    @Override
    public void rollback(Z input) {
      input.y.set(input.captured.get());
    }
  }



  @Test(expected = AssertionError.class)
  public void testExtractRefValueGraph() {

    X result = (X) ((PGraphComputer<X,X>)getGraphComputer()).computeWithInput(new ExtractRefValuePGraph(),new X());
  }

  public static class ExtractRefValuePGraph extends PGraph<X, X> {
    {
      pre(readConsume(X.class, x->x.y.get().name.equals("hello")));
      post(returns(X.class, x->x.y.get().name.equals("goodbye")));
      step(new ExtractRefValueGraphPEdge());
    }
  }


  public static class ExtractRefValueGraphPEdge extends PEdge<Y,Y> implements IRollback<Y>{
    {
      pre(readConsume(Y.class, y->y.name.equals("hello")));
      post(returns(Y.class, y->y.name.equals("goodbye")));
      // invalid code
      func(y->{
        y.name = "goodbye";
        return y;
      });
    }

    @Override
    public void capture(Y input) {

    }

    @Override
    public void rollback(Y input) {
      input.name = "hello";
    }
  }

}
