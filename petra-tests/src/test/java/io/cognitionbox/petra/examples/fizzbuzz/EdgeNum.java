package io.cognitionbox.petra.examples.fizzbuzz;

import io.cognitionbox.petra.lang.PEdge;

public class EdgeNum extends PEdge<X> {
    {
        type(X.class);
        pre(x -> true);
        func(x -> {
           x.addLine(String.valueOf(x));
        });
        post(x -> true);
    }
}