package io.cognitionbox.petra.examples.fizzbuzz;

import io.cognitionbox.petra.lang.PEdge;

public class EdgeFizz extends PEdge<X> {
    {
        type(X.class);
        pre(x -> x.i % 3 == 0);
        func(x -> x.addLine("Fizz"));
        post(x -> true);

    }
}
