package io.cognitionbox.petra.lang.impls.else_semantics;

import io.cognitionbox.petra.lang.PEdge;

public class Skip extends PEdge<Object> {
    {
        type(Object.class);
        pre(x -> true);
        func(x -> {
        });
        post(x -> true);
    }
}
