package io.cognitionbox.petra.lang.impls.steptest.init;

import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.lang.StepTest;
import org.junit.Test;

public class InitStepTest extends StepTest<Foo> {
    @Override
    protected Class<? extends IStep<Foo>> stepClass() {
        return IncrementMachine.class;
    }

    @Test
    public void test1() {
        setInput(new Foo());
        setExpectation(foo -> foo.result==1);
    }
}
