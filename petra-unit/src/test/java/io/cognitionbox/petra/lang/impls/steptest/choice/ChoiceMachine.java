package io.cognitionbox.petra.lang.impls.steptest.choice;

import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.seq;

public class ChoiceMachine extends PGraph<Foo> {
    {
        type(Foo.class);

        kase(foo -> foo.choices == Choices.A, foo -> (foo.choices == Choices.A && foo.result == 1));

        step(seq(), ActOnA.class);
        esak();

        kase(foo -> foo.choices == Choices.B, foo -> (foo.choices == Choices.B && foo.result == 2));

        step(seq(), ActOnB.class);
        esak();

        kase(foo -> foo.choices == Choices.C, foo -> (foo.choices == Choices.C && foo.result == 3));

        step(seq(), ActOnC.class);
        esak();
    }
}
