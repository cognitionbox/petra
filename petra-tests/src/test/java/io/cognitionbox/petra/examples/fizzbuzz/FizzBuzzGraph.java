package io.cognitionbox.petra.examples.fizzbuzz;


import io.cognitionbox.petra.lang.PGraph;

public class FizzBuzzGraph extends PGraph<X> {
    {
        type(X.class);
        pre(x -> x.isEmpty());
        step(new FizzBuzzEdge());
        post(x -> x.isFizzBuzz());
    }
}
