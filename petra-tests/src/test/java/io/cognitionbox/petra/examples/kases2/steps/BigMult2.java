package io.cognitionbox.petra.examples.kases2.steps;

import io.cognitionbox.petra.lang.math.IBigDecimalOp;
import io.cognitionbox.petra.lang.math.BigMult;
import io.cognitionbox.petra.util.function.IFunction;

import java.math.BigDecimal;

public class BigMult2<T extends IBigDecimalOp> extends BigMult<T> {
    public BigMult2(IFunction<T,BigDecimal> value)
    {
        super(value);
        type((Class<T>) IBigDecimalOp.class);
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