package io.cognitionbox.petra.examples.hawkeye.steps.hawkeye2;

import io.cognitionbox.petra.examples.hawkeye.objects.X;
import io.cognitionbox.petra.examples.kases3.objects.Something;
import io.cognitionbox.petra.lang.PEdge;

import static io.cognitionbox.petra.lang.math.BigDecimalUtils.r;

public class OneToTwo extends PEdge<X> {
    {
        type(X.class);
        kase(x -> x.i.result().equals(r(1)), x -> x.i.result().equals(r(2)));
        func(x ->{
           x.i.result(r(2));
        });
    }
}