package io.cognitionbox.petra.examples.simple2.helloworld;

import io.cognitionbox.petra.lang.PGraph;

public class AtoAGraph extends PGraph<X> {
    {
        type(X.class);
        pre(x -> x.isBlank());
        step(new AtoA());
        post(x -> x.isHelloWorld());
    }
}