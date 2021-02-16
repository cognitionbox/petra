package io.cognitionbox.petra.lang.impls.steptest.choice;

import io.cognitionbox.petra.lang.PEdge;

import static io.cognitionbox.petra.lang.impls.steptest.choice.Choices.*;

public class MakeChoice extends PEdge<Foo> {
    {
        type(Foo.class);
        pre(foo -> foo.choices==null);
        func(foo -> {
            foo.choices = C;
        });
        post(foo -> foo.choices!=null);
    }
}
