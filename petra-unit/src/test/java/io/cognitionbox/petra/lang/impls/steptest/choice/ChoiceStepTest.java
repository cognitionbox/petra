package io.cognitionbox.petra.lang.impls.steptest.choice;

import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.lang.StepTest;
import org.junit.Test;

public class ChoiceStepTest extends StepTest<Foo> {
    @Override
    protected Class<? extends IStep<Foo>> stepClass() {
        return ChoiceMachine.class;
    }

    @Test
    public void test1() {
        setInput(new Foo(Choices.A));
        setExpectation(foo -> foo.result==1);
    }

    @Test
    public void test2() {
        setInput(new Foo(Choices.B));
        setExpectation(foo -> foo.result==2);
    }

    @Test
    public void test3() {
        setInput(new Foo(Choices.C));
        setExpectation(foo -> foo.result==3);
    }
}
