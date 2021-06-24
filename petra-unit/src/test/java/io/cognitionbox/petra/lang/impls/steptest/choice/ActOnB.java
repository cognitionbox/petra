package io.cognitionbox.petra.lang.impls.steptest.choice;

import io.cognitionbox.petra.lang.PEdge;

public class ActOnB extends PEdge<Foo> {
    {
        type(Foo.class);
        kase(foo -> foo.choices == Choices.B, foo -> foo.result == 2);
        func(foo -> {
            foo.result = 2;
        });
    }
}
