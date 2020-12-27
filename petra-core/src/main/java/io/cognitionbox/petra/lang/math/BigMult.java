package io.cognitionbox.petra.lang.math;

import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.util.function.IFunction;

import java.math.BigDecimal;

import static io.cognitionbox.petra.lang.math.BigDecimalUtils.*;

public class BigMult<T extends IBigDecimalOp> extends PEdge<T> {
    public BigMult(IFunction<T,BigDecimal> value)
    {
        type((Class<T>) IBigDecimalOp.class);
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