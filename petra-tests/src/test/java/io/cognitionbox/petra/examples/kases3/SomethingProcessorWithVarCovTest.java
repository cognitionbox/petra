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
package io.cognitionbox.petra.examples.kases3;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.examples.kases3.objects.Something;
import io.cognitionbox.petra.examples.kases3.objects.Something2;
import io.cognitionbox.petra.examples.kases3.steps.SomethingProcessorWithVarCov;
import io.cognitionbox.petra.lang.*;
import io.cognitionbox.petra.util.Petra;
import org.javatuples.Pair;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Ignore
@RunWith(Parameterized.class)
public class SomethingProcessorWithVarCovTest extends StepTest<Something2> {
    public SomethingProcessorWithVarCovTest(ExecMode execMode) {
        super(execMode);
    }

    protected Supplier<AbstractStep<Something2>> stepSupplier(){
        return ()->new SomethingProcessorWithVarCov();
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

//        setInput(new Foo());
//        setExpectation(x->true);
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
        if (Petra.getVariables().size()!=0 &&
                Petra.getVariables().stream().filter(v->!v.getId().contains("RESULT")).allMatch(v->{
         if ((v instanceof RO) && (v instanceof RW)){
             return ((RO<?>) v).isRead() && ((RW<?>) v).isWritten();
         } else if (v instanceof RO){
             return ((RO<?>) v).isRead();
         } else {
             return false;
         }
        })){
            // ok
        } else {
            Petra.getVariables().stream().filter(v->!v.getId().contains("RESULT")).filter(v->{
                if ((v instanceof RO) && (v instanceof RW)){
                    return !(((RO<?>) v).isRead() && ((RW<?>) v).isWritten());
                } else if (v instanceof RO){
                    return !((RO<?>) v).isRead();
                } else {
                    return false;
                }
            }).forEach(v->System.out.println(((v instanceof RW)?"RW":"RO")+" "+v.getId()));
            throw new IllegalStateException("not all variables covered.");
        }
    }

    @Test
    public void test1() {
        setInput(new Something2(1));
        setExpectation(x->true);
    }

    @Test
    public void test2() {
        setInput(new Something2(6));
        setExpectation(x->true);
    }

//    @Test
//    public void test3() {
//        setInput(new Something(11));
//        setExpectation(x->true);
//    }
}
