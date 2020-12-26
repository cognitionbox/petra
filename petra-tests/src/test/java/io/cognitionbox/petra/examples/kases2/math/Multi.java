package io.cognitionbox.petra.examples.kases2.math;

import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.util.function.IFunction;

import java.math.BigDecimal;

import static io.cognitionbox.petra.examples.kases.math.R.*;

public class Multi<T extends IMath> extends PEdge<T> {
    public Multi(IFunction<T,BigDecimal> value)
    {
        type((Class<T>) IMath.class);
        kase(x -> isPos(x.result()) && isNeg(value.apply(x)), x -> isPos(x.result()));
        kase(x -> isPos(x.result()) && isPos(value.apply(x)), x -> isPos(x.result()));
        kase(x -> isPos(x.result()) && isNeg(value.apply(x)), x -> isNeg(x.result()));
        kase(x -> isNeg(x.result()) && isPos(value.apply(x)), x -> isNeg(x.result()));
        kase(x -> isZero(x.result()), x -> isZero(x.result()));
        kase(x -> isZero(value.apply(x)), x -> isZero(x.result()));
        func(x ->{
            x.result(x.result().multiply(value.apply(x)));
        });
    }
}