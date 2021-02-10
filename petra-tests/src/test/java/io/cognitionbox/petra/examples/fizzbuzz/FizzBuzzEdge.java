package io.cognitionbox.petra.examples.fizzbuzz;

import io.cognitionbox.petra.lang.PEdge;

public class FizzBuzzEdge extends PEdge<X> {
    {
        type(X.class);
        pre(x -> false);

        func(x -> {
            String line;
            int i = x.lineCount() + 1;
            if (i % 3 == 0 && i % 5 == 0) {
                line = "Fizz Buzz";
            } else if (i % 3 == 0) {
                line = "Fizz";
            } else {
                line = "Buzz";
            }

            x.addLine(line);
        });
        post(x -> true);
    }
}

