package io.cognitionbox.petra.examples.kases2.math;

import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.util.function.IFunction;

import java.math.BigDecimal;

import static io.cognitionbox.petra.examples.kases.math.R.notNull;

public class Assign<T extends IMath> extends PEdge<T> {
    public Assign(IFunction<T,BigDecimal> value)
    {
        type((Class<T>) IMath.class);
        kase(x ->notNull(value.apply(x)), x -> x.result().equals(value.apply(x)));
        func(x ->{
            x.result(value.apply(x));
        });
    }
}