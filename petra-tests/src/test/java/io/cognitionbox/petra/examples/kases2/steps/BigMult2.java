package io.cognitionbox.petra.examples.kases2.steps;

import io.cognitionbox.petra.lang.math.Nullary;
import io.cognitionbox.petra.lang.math.BigMult;
import io.cognitionbox.petra.util.function.IFunction;

import java.math.BigDecimal;

public class BigMult2<T extends Nullary> extends BigMult<T> {
    public BigMult2(IFunction<T,BigDecimal> value)
    {
        super(value);
        type((Class<T>) Nullary.class);
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