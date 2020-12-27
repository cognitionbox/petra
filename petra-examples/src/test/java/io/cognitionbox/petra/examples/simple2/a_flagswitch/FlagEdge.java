package io.cognitionbox.petra.examples.simple2.a_flagswitch;

import io.cognitionbox.petra.lang.PEdge;

public class FlagEdge extends PEdge<X> {
    {
        type(X.class);
        pre(x -> x.value == false);
        func(x -> {
            x.value = true;
        });
        post(x -> x.value == true);
    }
}