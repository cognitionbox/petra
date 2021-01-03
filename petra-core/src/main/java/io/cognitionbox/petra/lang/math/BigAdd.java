package io.cognitionbox.petra.lang.math;

import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.util.function.IFunction;

import java.math.BigDecimal;

import static io.cognitionbox.petra.lang.math.BigDecimalUtils.*;

public class BigAdd<T extends Nullary> extends PEdge<T> {
    public BigAdd(IFunction<T,BigDecimal> value)
    {
        type((Class<T>) Nullary.class);
        kase(x -> true, x -> true);
        func(x ->{
            x.result(x.result().add(value.apply(x)));
        });
    }
}