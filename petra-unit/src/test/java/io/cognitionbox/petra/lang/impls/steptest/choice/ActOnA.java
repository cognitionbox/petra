package io.cognitionbox.petra.lang.impls.steptest.choice;

import io.cognitionbox.petra.lang.PEdge;

public class ActOnA extends PEdge<Foo> {
    {
        type(Foo.class);
        pre(foo -> foo.choices == Choices.A);
        func(foo -> {
            foo.result = 1;
        });
        post(foo -> foo.result == 1);
    }
}
