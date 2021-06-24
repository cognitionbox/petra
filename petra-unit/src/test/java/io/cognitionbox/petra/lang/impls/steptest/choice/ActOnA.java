package io.cognitionbox.petra.lang.impls.steptest.choice;

import io.cognitionbox.petra.lang.PEdge;

public class ActOnA extends PEdge<Foo> {
    {
        type(Foo.class);
        kase(foo -> foo.choices == Choices.A, foo -> foo.result == 1);
        func(foo -> {
            foo.result = 1;
        });
    }
}
