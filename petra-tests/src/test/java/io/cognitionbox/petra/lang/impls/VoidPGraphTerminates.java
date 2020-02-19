/*
 ********************************************************************************************************
 * Copyright © 2016-2020 Cognition Box Ltd. - All Rights Reserved
 *
 * This file is part of the "Petra" system. "Petra" is owned by:
 *
 * Cognition Box Ltd. (10194162)
 * 9 Grovelands Road,
 * Palmers Green,
 * London, N13 4RJ
 * England.
 *
 * "Petra" is Proprietary and Confidential.
 * Unauthorized copying of "Petra" files, via any medium is strictly prohibited.
 *
 * "Petra" can not be copied and/or distributed without the express
 * permission of Cognition Box Ltd.
 *
 * "Petra" includes trade secrets of Cognition Box Ltd.
 * In order to protect "Petra", You shall not decompile, reverse engineer, decrypt,
 * extract or disassemble "Petra" or otherwise reduce or attempt to reduce any software
 * in "Petra" to source code form. You shall ensure, both during and
 * (if you still have possession of "Petra") after the performance of this Agreement,
 * that (i) persons who are not bound by a confidentiality agreement consistent with this Agreement
 * shall not have access to "Petra" and (ii) persons who are so bound are put on written notice that
 * "Petra" contains trade secrets, owned by and proprietary to Cognition Box Ltd.
 *
 * "Petra" is written by Aran Hakki <aran@cognitionbox.io>
 ********************************************************************************************************
 */
package io.cognitionbox.petra.lang.impls;

import io.cognitionbox.petra.lang.RGraphComputer;
import io.cognitionbox.petra.lang.Void;
import io.cognitionbox.petra.lang.annotations.Extract;
import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


import java.io.Serializable;

import static io.cognitionbox.petra.util.Petra.readConsume;
import static org.assertj.core.api.Java6Assertions.assertThat;


@RunWith(Parameterized.class)
public class VoidPGraphTerminates extends BaseExecutionModesTest {


    public VoidPGraphTerminates(ExecMode execMode) {
        super(execMode);
    }

    @Extract
    public static class Person implements Serializable {
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
    public static class ProcessPerson extends PGraph<Person, Void> {
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
