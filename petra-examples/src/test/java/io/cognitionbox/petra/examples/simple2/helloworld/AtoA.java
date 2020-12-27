package io.cognitionbox.petra.examples.simple2.helloworld;

import io.cognitionbox.petra.lang.PEdge;

public class AtoA extends PEdge<X> {
    {
        type(X.class);
        pre(x -> x.isBlankOrHelloWorld());
        func(x -> {
            x.value = "hello world.";
        });
        post(x -> x.isHelloWorld());
    }
}