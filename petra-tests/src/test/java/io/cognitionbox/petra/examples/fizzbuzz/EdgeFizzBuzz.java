package io.cognitionbox.petra.examples.fizzbuzz;

import io.cognitionbox.petra.lang.PEdge;

public class EdgeFizzBuzz extends PEdge<X> {
    {
        type(X.class);
        pre(x -> x.i % 3 == 0 && x.i % 5 == 0);
        func(x -> x.addLine("FizzBuzz"));
        post(x -> true);

    }
}
