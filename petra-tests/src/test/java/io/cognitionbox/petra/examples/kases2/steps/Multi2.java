package io.cognitionbox.petra.examples.kases2.steps;

import io.cognitionbox.petra.examples.kases2.math.IMath;
import io.cognitionbox.petra.examples.kases2.math.Multi;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.util.function.IFunction;

import java.math.BigDecimal;

import static io.cognitionbox.petra.examples.kases.math.R.*;

public class Multi2<T extends IMath> extends Multi<T> {
    public Multi2(IFunction<T,BigDecimal> value)
    {
        super(value);
        type((Class<T>) IMath.class);
        ignoreKase(0);
        ignoreKase(2);
        ignoreKase(3);
        ignoreKase(4);
        ignoreKase(5);
        func(x ->{
            x.result(x.result().multiply(value.apply(x)));
        });
    }
}