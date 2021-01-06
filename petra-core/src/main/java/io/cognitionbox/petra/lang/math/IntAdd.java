package io.cognitionbox.petra.lang.math;

import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.util.function.IFunction;

public class IntAdd<T extends IIntegerOp> extends PEdge<T> {
    public IntAdd(IFunction<T,Integer> value)
    {
        type((Class<T>) IIntegerOp.class);
        kase(x -> true, x -> true);
        func(x ->{
            x.result(x.result()+value.apply(x));
        });
    }
}