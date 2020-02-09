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
        RGraphComputer.getConfig().setConstructionGuaranteeChecks(false);
        Person person = new Person("Aran",32);
        Object result = PGraphComputer.computeWithInput(new ProcessPerson(),person);
        assertThat(result).isInstanceOf(Void.class);
    }

}
