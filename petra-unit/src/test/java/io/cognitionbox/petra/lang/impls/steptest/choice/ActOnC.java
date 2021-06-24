package io.cognitionbox.petra.lang.impls.steptest.choice;

import io.cognitionbox.petra.lang.PEdge;

public class ActOnC extends PEdge<Foo> {
    {
        type(Foo.class);
        kase(foo -> foo.choices == Choices.C, foo -> foo.result == 3);
        func(foo -> {
            foo.result = 3;
        });
    }
}
