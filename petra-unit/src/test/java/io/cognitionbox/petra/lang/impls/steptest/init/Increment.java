package io.cognitionbox.petra.lang.impls.steptest.init;

import io.cognitionbox.petra.lang.PEdge;

public class Increment extends PEdge<Foo> {
    {
        type(Foo.class);
        pre(foo -> true);
        func(foo -> {
            foo.result++;
        });
        post(foo -> true);
    }
}
