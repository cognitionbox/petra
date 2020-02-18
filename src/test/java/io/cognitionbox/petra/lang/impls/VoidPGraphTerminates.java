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

import io.cognitionbox.petra.lang.RGraphComputer;
import io.cognitionbox.petra.lang.Void;
import io.cognitionbox.petra.lang.annotations.Extract;
import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.PGraph;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


import static io.cognitionbox.petra.util.Petra.readConsume;
import static org.assertj.core.api.Java6Assertions.assertThat;


@RunWith(Parameterized.class)
public class VoidPGraphTerminates extends BaseExecutionModesTest {


    public VoidPGraphTerminates(ExecMode execMode) {
        super(execMode);
    }

    @Extract
    public static class Person {
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

        public Person(String name, Integer age) {
            this.name = name;
            this.age = age;
        }
    }

    public static class PrintAge extends PEdge<Integer, Void> {
        {
            pre(readConsume(Integer.class, x->true));
            func(x->{System.out.println("age="+x);return Void.vd;});
            postVoid();
        }
    }

    public static class PrintName extends PEdge<String, Void> {
        {
            pre(readConsume(String.class, x->true));
            func(x->{System.out.println("name="+x);return Void.vd;});
            postVoid();
        }
    }

    // how to terminate with a void someRef…? Need to check this in the code
    public static class ProcessPerson extends PGraph<@Extract Person, Void> {
        {
            pre(readConsume(Person.class, x->true));
            step(new PrintAge());
            step(new PrintName());
            postVoid();
        }
    }

    @Test
    public void terminatesWithVOID() {
        getGraphComputer().getConfig().setConstructionGuaranteeChecks(false);
        Person person = new Person("Aran",32);
        Object result = getGraphComputer().computeWithInput(new ProcessPerson(),person);
        assertThat(result).isInstanceOf(Void.class);
    }

}
