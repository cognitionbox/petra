package io.cognitionbox.petra.lang.impls.else_semantics;

import io.cognitionbox.petra.lang.PEdge;

public class AtoBEdge extends PEdge<Value> {
    {
        type(Value.class);
        kase(x -> x.value == "A", x -> x.value == "B");
        func(x -> {
            x.value = "B";
        });
    }
}
