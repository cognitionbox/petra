package io.cognitionbox.petra.lang.math;

import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.util.function.IFunction;

public class Assign<T extends IOp> extends PEdge<T> {
    public Assign(IFunction<T,?> value)
    {
        type((Class<T>) IOp.class);
        kase(x -> value.apply(x)!=null, x -> x.result().equals(value.apply(x)));
        func(x ->{
            x.result(value.apply(x));
        });
    }
}