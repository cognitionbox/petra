///**
// * Copyright (C) 2016-2020 Aran Hakki.
// *
// * This file is part of Petra.
// *
// * Petra is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// *
// * Petra is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public License
// * along with Petra.  If not, see <https://www.gnu.org/licenses/>.
// */
//package io.cognitionbox.petra.examples.simple.compose;
//
//
//import io.cognitionbox.petra.config.ExecMode;
//import io.cognitionbox.petra.lang.*;
//import io.cognitionbox.petra.lang.annotations.Extract;
//import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
//import io.cognitionbox.petra.util.Petra;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.Parameterized;
//
//import static io.cognitionbox.petra.util.Petra.rc;
//import static io.cognitionbox.petra.util.Petra.rt;
//
//@RunWith(Parameterized.class)
//public class ComposeMain2 extends BaseExecutionModesTest {
//    public ComposeMain2(ExecMode execMode) {
//        super(execMode);
//    }
//    /*
//     * This demonstrates disorderly programming.
//     * The steps can be defined in any order and the outcome is always the same.
//     * It shows a more logical way to program.
//     *
//     * Will print out E and F and then terminate.
//     *
//     * First A is consumed,
//     * then A is transformed to B,
//     * then B is transformed to C,
//     * then C is tranformed to D (which contains E amd F)
//     * then D is deconstucted into E and F
//     * then E and F are printed
//     * then it terminates
//     */
//
//    interface PreA{}
//    interface PostA{}
//    interface PreB{}
//    interface PostB{}
//
//    public static class A implements PreA, PostA{}
//    public static class B implements PreB, PostB{}
//    @Extract class X{
//        @Extract public A a(){return null;}
//        @Extract public B b(){return null;}
//    }
//    public static class Y{}
//    public static class AtoA extends PEdge<A, A> {
//        {
//            pre(rc(PreA.class, a->true));
//            func(a->a);
//            post(Petra.rt(PostA.class, a->true));
//        }
//    }
//    public static class BtoB extends PEdge<B, B> {
//        {
//            pre(rc(PreB.class, b->true));
//            func(b->b);
//            post(Petra.rt(PostB.class, b->true));
//        }
//    }
//    public static class ABtoZ extends PJoin2<A, B, Y> {
//        {
//            preA(rc(PostA.class, a->true));
//            preB(rc(PostB.class, b->true));
//            func((a,b)->new Y());
//            post(Petra.rt(Y.class, y->true));
//        }
//    }
//    public static class XtoY extends PGraph<X, Y> {
//        {
//            pre(rc(X.class, x->true));
//            step(new AtoA());
//            step(new BtoB());
//            joinSome(new ABtoZ());
//            post(Petra.rt(Y.class, y->true));
//        }
//    }
//
//    @Test
//    public void test(){
//        PGraphComputer.getConfig().enableStatesLogging();
//
//
//
//        Y result = new PGraphComputer<X, Y>().computeWithInput(new XtoY(),new X());
//        System.out.println(result);
//    }
//}