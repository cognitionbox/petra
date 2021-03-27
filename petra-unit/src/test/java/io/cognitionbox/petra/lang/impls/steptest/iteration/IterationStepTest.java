package io.cognitionbox.petra.lang.impls.steptest.iteration;

import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.lang.StepTest;
import org.junit.Test;

public class IterationStepTest extends StepTest<Foo> {
    @Override
    protected Class<? extends IStep<Foo>> stepClass() {
        return IterationMachine.class;
    }

    @Test
    public void test1() {
        setInput(new Foo());
        setExpectation(foo -> foo.result==3);
    }
}
