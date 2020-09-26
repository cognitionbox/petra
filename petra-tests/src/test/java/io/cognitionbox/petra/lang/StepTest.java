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

import io.cognitionbox.petra.core.impl.*;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import io.cognitionbox.petra.exceptions.EdgeException;
import io.cognitionbox.petra.exceptions.IterationsTimeoutException;
import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.core.engine.petri.impl.Token;
import org.javatuples.Pair;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static io.cognitionbox.petra.util.Petra.rt;
import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

public abstract class StepTest<X> extends BaseExecutionModesTest {

    private static final Logger LOG = LoggerFactory.getLogger(StepTest.class);
    private static PGraphDotDiagramRendererImpl2 renderer;
    private static Map<String,String> dotToRender = new ConcurrentHashMap<>();

    {
        renderer = new PGraphDotDiagramRendererImpl2();
    }

    public StepTest(ExecMode execMode) {
        super(execMode);
    }

    @Before
    public void before(){
        stepFixture = new StepFixture(stepSupplier().get(),getExecMode());
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

    abstract Supplier<AbstractStep<X>> stepSupplier();

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

}
