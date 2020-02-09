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
package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.guarantees.impl.GraphOutputCannotBeReachedFromInput;
import io.cognitionbox.petra.lang.annotations.Extract;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.util.Petra;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.cognitionbox.petra.util.Petra.readConsume;
import static io.cognitionbox.petra.util.Petra.returns;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ReachabilityHelperTest extends BaseExecutionModesTest {

    public ReachabilityHelperTest(ExecMode execMode) {
        super(execMode);
    }

    interface X{}
    interface Y{}

    static class A implements X{}

    static class B implements X{}

    @Extract
    static class C implements Y{
        String name;

        @Extract
        public String name(){
            return name;
        }
        public C(String name) {
            this.name = name;
        }
    }

    @Extract
    static class D implements Y{
        String name;

        @Extract
        public String name(){
            return name;
        }


        Integer age;
        @Extract
        public Integer age(){
            return age;
        }

        public D(String name, Integer age) {
            this.name = name;
            this.age = age;
        }
    }

    public static class XtoYPJoin extends PJoin2<C,String,C> {
        {
            preA(readConsume(C.class, x->true));
            preB(readConsume(String.class, x->true));
            func((y, s)->new C("Aran"));
            post(Petra.returns(C.class, x->true));
        }
    }

    public static class XtoYPEdge extends PEdge<A,C> {
        {
            pre(readConsume(A.class, x->true));
            func(x->new C("Aran"));
            post(Petra.returns(C.class, x->true));
        }
    }

    public static class XtoY extends PGraph<A,C> {
        {
            pre(readConsume(A.class, x->true));
            step(new XtoYPEdge());
            joinSome(new XtoYPJoin());
            post(Petra.returns(C.class, x->true));
        }
    }

    @Test
    public void testReachabilityChecker() {
       GraphOutputCannotBeReachedFromInput g = new GraphOutputCannotBeReachedFromInput();
       assertThat(g.test(new XtoY())).isFalse();
    }
}