package io.cognitionbox.petra.lang.math;

import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.util.function.IFunction;

import java.math.BigDecimal;

import static io.cognitionbox.petra.lang.math.BigDecimalUtils.*;

public class BigAdd<T extends Nullary> extends PEdge<T> {
    public BigAdd(IFunction<T,BigDecimal> value)
    {
        type((Class<T>) Nullary.class);
        kase(x->isPos(x.result()) && isPos(value.apply(x)), x->isPos(x.result()));
        kase(x->isNeg(x.result()) && isNeg(value.apply(x)), x->isNeg(x.result()));
        kase(x->isPos(x.result()) && isNeg(value.apply(x)) && isAbsGreaterThan(value.apply(x),x.result()), x->isNeg(x.result()));
        kase(x->isPos(x.result()) && isNeg(value.apply(x)) && isAbsGreaterThan(x.result(),value.apply(x)), x->isPos(x.result()));
        kase(x->isNeg(x.result()) && isPos(value.apply(x)) && isAbsGreaterThan(value.apply(x),x.result()), x->isPos(x.result()));
        kase(x->isNeg(x.result()) && isPos(value.apply(x)) && isAbsGreaterThan(x.result(),value.apply(x)), x->isNeg(x.result()));
        func(x ->{
            x.result(x.result().add(value.apply(x)));
        });
    }
}