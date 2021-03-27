package io.cognitionbox.petra.lang.impls.steptest.choice;

import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.choice;

public class ChoiceMachine extends PGraph<Foo> {
    {
        type(Foo.class);
        pre(foo -> foo.choices == null);
        begin();
        step(foo -> foo, MakeChoice.class);
        step(choice(), ActOnA.class);
        step(choice(), ActOnB.class);
        step(choice(), ActOnC.class);
        end();
        post(foo -> (foo.choices == Choices.A && foo.result == 1) ^
                (foo.choices == Choices.B && foo.result == 2) ^
                (foo.choices == Choices.C && foo.result == 3));
    }
}
