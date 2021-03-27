package io.cognitionbox.petra.lang.impls.hawkeye.steps.hawkeye2;

import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.impls.hawkeye.objects.X;

import java.math.BigDecimal;

public class FourToEight extends PEdge<X> {
    {
        type(X.class);
        kase(x -> x.isFour(), x -> x.isEight());
        func(x ->{
           x.i = BigDecimal.valueOf(8);
        });
    }
}