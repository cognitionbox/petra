package io.cognitionbox.petra.lang.impls.steptest.choice;

import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.lang.StepTest;
import org.junit.Ignore;
import org.junit.Test;

public class ChoiceStepTest extends StepTest<Foo> {
    @Override
    protected Class<? extends IStep<Foo>> stepClass() {
        return ChoiceMachine.class;
    }

    @Ignore // temporary for refactor
    @Test
    public void test1() {
        setInput(new Foo());
        setExpectation(foo -> true);
    }
}
