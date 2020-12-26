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
package io.cognitionbox.petra.examples.kases2;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.examples.kases2.objects.Foo;
import io.cognitionbox.petra.examples.kases2.steps.FooSum;
import io.cognitionbox.petra.examples.kases2.steps.FooSum2;
import io.cognitionbox.petra.lang.*;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import org.javatuples.Pair;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RunWith(Parameterized.class)
public class KasesStepTest extends StepTest<Foo> {
    public KasesStepTest(ExecMode execMode) {
        super(execMode);
    }

    protected Supplier<AbstractStep<Foo>> stepSupplier(){
        return ()->new FooSum2();
    }

    @Test
    public void zappedAllKases(){

        List<Method> tests = new ArrayList<>();
        for (Method m : this.getClass().getDeclaredMethods()){
            if (m.isAnnotationPresent(Test.class)){
                tests.add(m);
            }
        }
        if (!tests.stream().sorted(Comparator.comparing(Method::getName).reversed()).findFirst().get().getName().equals("zappedAllKases")){
            throw new IllegalStateException("other method name exists which comes after zappedAllKases.");
        }

        setInput(new Foo());
        setExpectation(x->true);
        long requiredToCover = kases.stream().filter(k->k.getStep() instanceof PEdge).filter(k->!ignoredkases.contains(Pair.with(k.getStep(),k.getId()))).count();
        long covered = kases.stream().filter(k->k.getStep() instanceof PEdge).filter(k->k.isCovered()).count();
        if (kases.size()==0 || covered!=requiredToCover){
            Set<RGraph> steps = kases.stream().filter(k->!(k.getStep() instanceof PEdge))
                    .map(k->(RGraph)k.getStep())
                    .collect(Collectors.toSet());
            for (RGraph<?,?> s : steps){
                s.getParallizable().stream()
                        .flatMap(stp->(Stream<Kase>)((AbstractStep)stp).getKases().stream())
                        .filter(k->!ignoredkases.contains(Pair.with(k.getStep(),k.getId())))
                        .filter(k->!k.isCovered())
                        .forEach(k->System.out.println(s.getStepClazz().getSimpleName()+" "+Pair.with(k.getStep().getStepClazz().getSimpleName(),k.getId())));
            }
            throw new IllegalStateException("not all kases covered.");
        }
    }

    @Test
    public void test1() {
        setInput(new Foo());
        setExpectation(x->true);
    }
}
