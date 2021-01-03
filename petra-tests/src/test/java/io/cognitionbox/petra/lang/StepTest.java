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

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.core.engine.petri.impl.Token;
import io.cognitionbox.petra.core.impl.AbstractRO;
import io.cognitionbox.petra.core.impl.Identifyable;
import io.cognitionbox.petra.core.impl.PGraphDotDiagramRendererImpl2;
import io.cognitionbox.petra.exceptions.EdgeException;
import io.cognitionbox.petra.exceptions.IterationsTimeoutException;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import io.cognitionbox.petra.util.Petra;
import io.cognitionbox.petra.util.function.IPredicate;
import org.javatuples.Pair;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class StepTest<X> extends BaseExecutionModesTest {

    private static final Logger LOG = LoggerFactory.getLogger(StepTest.class);
    private static PGraphDotDiagramRendererImpl2 renderer;
    private static Map<String,String> dotToRender = new ConcurrentHashMap<>();

    @Rule public TestName testName = new TestName();

    {
        renderer = new PGraphDotDiagramRendererImpl2();
    }

    public StepTest(ExecMode execMode) {
        super(execMode);
    }


    private static void collectSteps(PGraph<?> graph, Set<AbstractStep> steps) {
        steps.add(graph);
        for (IStep g : graph.getParallizable()) {
            if (g instanceof RGraph) {
                collectSteps((PGraph<?>) g, steps);
            } else {
                //if (g instanceof PEdge){
                    steps.add((AbstractStep) g);
                //}
            }
        }
    }

    private volatile AbstractStep step = null;
    public volatile Set<Kase> kases = Collections.newSetFromMap(new ConcurrentHashMap<>());
    public volatile Set<Pair<Class<? extends IStep>,Integer>> ignoredkases = Collections.newSetFromMap(new ConcurrentHashMap<>());
    public static volatile List<Kase> allKases = new ArrayList<>();

    @BeforeClass
    public static void beforeClass() {
        allKases.clear();
        Petra.getVariables().clear();
    }

    @Before
    public void before(){
        AbstractRO.atomicIntegerMap.clear();
        step = stepSupplier().get();
        Set<AbstractStep> steps = new HashSet<>();
        collectSteps((PGraph<?>)step,steps);
        kases = steps.stream().flatMap(s->(Stream<Kase>)s.getKases().stream()).collect(toSet());
        allKases.addAll(kases);
        ignoredkases = steps.stream().flatMap(s->(Stream<Pair<Class<? extends IStep>,Integer>>)s.getIgnoredKases().stream()).collect(toSet());
        stepFixture = new StepFixture(step,getExecMode());
    }


    public static class EdgePGraph extends PGraph<Object> {
        EdgePGraph(PEdge PEdge){
            type(Object.class);
            pre(x->true);
            step(PEdge);
            post(x->true);
        }
    }

    public static class StepFixture<X> {
        private AbstractStep<X> step;
        private X in;
        private X out;

        private void setInput(X in) {
            this.in = in;
        }

        private X getOutput() {
            return out;
        }

        private X getInput() {
            return in;
        }

        ExecMode execMode;
        private StepFixture(AbstractStep<X> step, ExecMode execMode) {
            this.step = step;
            this.execMode = execMode;
        }
        private void execute(){
            if (step instanceof PGraph && execMode.isDIS()) {
                PComputer computer;
                computer = new PComputer<>();
                out = (X) computer.eval((RGraph) step, in);
                computer.shutdown();
            } else if (step instanceof PEdge && execMode.isDIS()){
                PComputer computer;
                computer = new PComputer<>();
                out = (X) computer.eval(new EdgePGraph((PEdge) step), in);
                computer.shutdown();
            } else {
                step.setInput(new Token(in));
                try {
                    out = (X) step.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // render
            if (step instanceof PGraph) {
                renderer.render((RGraph) step);
            } else if (step instanceof PEdge){
                renderer.render(new EdgePGraph((PEdge) step));
            }
        }
    }
    private StepFixture<X> stepFixture;

    protected void setInput(X input){
        stepFixture.setInput(input);
    }

    private Predicate<X> expectation;
    protected void setExpectation(Predicate<X> expectation){
        if (stepFixture.getInput()==null){
            throw new IllegalStateException("no input set!");
        }
        this.expectation = expectation;
        stepFixture.execute();
    }

    protected abstract Supplier<AbstractStep<X>> stepSupplier();

    public static int comp(String s1, String s2) {
        if (s1.contains("red") && !s2.contains("red")){
            return 1;
        } else if (!s1.contains("red") && s2.contains("red")){
            return -1;
        } else if (!s1.contains("red") && !s2.contains("red")){
            return 0;
        } else if (s1.contains("red") && s2.contains("red")){
            return 0;
        }
        return 0;
    }

    private void printStackTrace(Throwable e){
        e.printStackTrace();
    }

    @After
    public void after(){
        if (testName.getMethodName().contains("zappedAllKases")){
            return;
        }
        X out = stepFixture.getOutput();
        if (out==null){
            fail("output is null");
        }
        boolean passes = false;
        if (stepFixture.step instanceof RGraph) {
            if (out instanceof IterationsTimeoutException) {
                passes = false; // break when find all exceptions mode
            } else {
                passes = true;
            }
        } else if (stepFixture.step instanceof PEdge) {
            if (out instanceof EdgeException) {
                printStackTrace((Throwable) out);
                passes = false; // break when find all exceptions mode
            } else {
                passes = true;
            }
        }

        if (passes){
            passes = expectation.test(out);
        }
        if (passes){
            String desc = ((Identifyable) stepFixture.step).getPartitionKey();
            //LOG.info("PASSES: "+desc);
            String value = desc+" [shape=rect style=filled, fillcolor=green];\n";
            dotToRender.put(value,value);

        } else {
            String desc = ((Identifyable) stepFixture.step).getPartitionKey();
            //LOG.info("FAILS: "+desc);
            String value = desc+" [shape=rect style=filled, fillcolor=red];\n";
            dotToRender.put(value,value);
        }
        assertTrue(passes);
    }

    @AfterClass
    public static void staticAfter(){
        dotToRender.values().stream().sorted(StepTest::comp).forEach(s->renderer.append(s,null));
        renderer.finish();
        System.out.println(renderer.getDotOutput());
    }

    @Test
    public void zappedAllKases(){

        List<Method> tests = new ArrayList<>();
        for (Method m : this.getClass().getMethods()){
            if (m.isAnnotationPresent(Test.class)){
                tests.add(m);
            }
        }
        if (!tests.stream().sorted(Comparator.comparing(Method::getName).reversed()).findFirst().get().getName().equals("zappedAllKases")){
            throw new IllegalStateException("other method name exists which comes after zappedAllKases.");
        }

//        setInput(new Foo());
//        setExpectation(i->true);

        // .filter(k->k.getStep() instanceof PEdge)
        long requiredToCover = kases.stream().filter(k->!ignoredkases.contains(Pair.with(k.getStep().getStepClazz(),k.getId()))).count();

        Set<Kase> requiredToCoverSet = kases.stream().filter(k->!ignoredkases.contains(Pair.with(k.getStep().getStepClazz(),k.getId()))).collect(toSet());

        // .filter(k->k.getStep() instanceof PEdge)
        long covered = allKases.stream().filter(k->k.isCovered()).count();

        Set<Kase> coveredSet = allKases.stream().filter(k->k.isCovered()).collect(toSet());

        Set<Kase> notCoveredSet = allKases.stream().filter(k->!k.isCovered()).collect(toSet());

        Set<Kase> diffSet = new HashSet<>(notCoveredSet);
        diffSet.removeAll(coveredSet);

        if (covered==0 || !coveredSet.equals(requiredToCoverSet)){
            Set<RGraph> steps = kases.stream().filter(k->!(k.getStep() instanceof PEdge))
                    .map(k->(RGraph)k.getStep())
                    .collect(Collectors.toSet());

            Set<Kase> finalSet = new HashSet<>();
            for (RGraph<?,?> s : steps){

                finalSet.addAll(diffSet.stream()
                        .filter(k->s.getParallizable().stream().flatMap(stp->(Stream<Kase>)((AbstractStep)stp).getKases().stream()).collect(toSet()).contains(k) && !ignoredkases.contains(Pair.with(k.getStep().getStepClazz(),k.getId())))
                        .filter(k->!k.isCovered())
                        .peek(k->System.out.println(s.getStepClazz().getSimpleName()+" "+Pair.with(k.getStep().getStepClazz().getSimpleName(),k.getId())))
                        .collect(toSet()));

//                s.getParallizable().stream()
//                        .flatMap(stp->(Stream<Kase>)((AbstractStep)stp).getKases().stream())
//                        .filter(k->!ignoredkases.contains(Pair.with(k.getStep(),k.getId())))
//                        .filter(k->!k.isCovered())
//                        .forEach(k->System.out.println(s.getStepClazz().getSimpleName()+" "+Pair.with(k.getStep().getStepClazz().getSimpleName(),k.getId())));
            }

            if (!finalSet.isEmpty()){
                throw new IllegalStateException("not all kases covered.");
            }
        }
        IPredicate<V> coveredPred = v->{
            if ((v instanceof RO) && (v instanceof RW)){
                return (((RO<?>) v).isRead() && ((RW<?>) v).isWritten());
            } else if (v instanceof RO){
                return ((RO<?>) v).isRead();
            } else {
                return false;
            }
        };
        List<V> coveredList = Petra.getVariables().stream().filter(coveredPred).filter(v->!v.getId().contains("RESULT")).collect(Collectors.toList());
        List<V> notCoveredList = Petra.getVariables().stream().filter(coveredPred.negate()).filter(v->!v.getId().contains("RESULT")).collect(Collectors.toList());

        Set<String> diffList = notCoveredList.stream().map(v->v.getVariableNumberStepName()).collect(toSet());
        diffList.removeAll(coveredList.stream().map(v->v.getVariableNumberStepName()).collect(toSet()));

        if (diffList.isEmpty()){
            // ok
        } else {
            diffList.stream().forEach(v->System.out.println(v));
            throw new IllegalStateException("not all variables covered.");
        }
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

}
