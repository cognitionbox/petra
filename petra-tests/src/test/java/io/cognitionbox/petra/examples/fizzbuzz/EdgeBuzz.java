package io.cognitionbox.petra.examples.fizzbuzz;

import io.cognitionbox.petra.lang.PEdge;

public class EdgeBuzz extends PEdge<X> {
    {
        type(X.class);
        pre(x-> x.i % 5 == 0);
        func(x -> x.addLine("Buzz"));
        post(x -> true);

    }
}
