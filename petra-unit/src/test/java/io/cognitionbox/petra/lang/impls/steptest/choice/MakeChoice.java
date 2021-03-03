package io.cognitionbox.petra.lang.impls.steptest.choice;

import io.cognitionbox.petra.lang.PEdge;

public class MakeChoice extends PEdge<Foo> {
    {
        type(Foo.class);
        pre(foo -> foo.choices == null);
        func(foo -> {
            foo.choices = Choices.C;
        });
        post(foo -> foo.choices != null);
    }
}
