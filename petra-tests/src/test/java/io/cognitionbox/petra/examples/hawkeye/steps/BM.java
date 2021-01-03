package io.cognitionbox.petra.examples.hawkeye.steps;

import io.cognitionbox.petra.lang.math.BigMult;
import io.cognitionbox.petra.lang.math.Nullary;
import io.cognitionbox.petra.util.function.IFunction;

import java.math.BigDecimal;

public class BM<T extends Nullary> extends BigMult<T> {
    public BM(IFunction<T,BigDecimal> value)
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