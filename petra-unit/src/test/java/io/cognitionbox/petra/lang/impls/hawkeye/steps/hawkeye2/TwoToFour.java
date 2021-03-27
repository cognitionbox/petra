package io.cognitionbox.petra.lang.impls.hawkeye.steps.hawkeye2;

import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.impls.hawkeye.objects.X;

import java.math.BigDecimal;

public class TwoToFour extends PEdge<X> {
    {
        type(X.class);
        kase(x -> x.isTwo(), x -> x.isFour());
        func(x ->{
           x.i = BigDecimal.valueOf(4);
        });
    }
}