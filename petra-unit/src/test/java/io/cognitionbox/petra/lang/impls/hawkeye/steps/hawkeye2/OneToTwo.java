package io.cognitionbox.petra.lang.impls.hawkeye.steps.hawkeye2;

import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.impls.hawkeye.objects.X;

import java.math.BigDecimal;

public class OneToTwo extends PEdge<X> {
    {
        type(X.class);
        kase(x -> x.isOne(), x -> x.isTwo());
        func(x ->{
           x.i = BigDecimal.valueOf(2);
        });
    }
}