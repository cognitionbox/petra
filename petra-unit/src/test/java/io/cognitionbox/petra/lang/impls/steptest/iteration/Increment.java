package io.cognitionbox.petra.lang.impls.steptest.iteration;

import io.cognitionbox.petra.lang.PEdge;

public class Increment extends PEdge<Foo> {
    {
        type(Foo.class);
        kase(foo -> true, foo -> true);
        func(foo -> {
            foo.result++;
        });
    }
}
