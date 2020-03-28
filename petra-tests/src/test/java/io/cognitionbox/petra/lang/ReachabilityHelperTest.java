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
package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.guarantees.impl.GraphOutputCannotBeReachedFromInput;
import io.cognitionbox.petra.lang.annotations.Extract;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.util.Petra;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.cognitionbox.petra.util.Petra.rc;
import static io.cognitionbox.petra.util.Petra.rt;
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
            preA(rc(C.class, x->true));
            preB(rc(String.class, x->true));
            func((y, s)->new C("Aran"));
            post(Petra.rt(C.class, x->true));
        }
    }

    public static class XtoYPEdge extends PEdge<A,C> {
        {
            pre(rc(A.class, x->true));
            func(x->new C("Aran"));
            post(Petra.rt(C.class, x->true));
        }
    }

    public static class XtoY extends PGraph<A,C> {
        {
            pre(rc(A.class, x->true));
            step(new XtoYPEdge());
            joinSome(new XtoYPJoin());
            post(Petra.rt(C.class, x->true));
        }
    }

    @Test
    public void testReachabilityChecker() {
       GraphOutputCannotBeReachedFromInput g = new GraphOutputCannotBeReachedFromInput();
       assertThat(g.test(new XtoY())).isFalse();
    }
}