package io.cognitionbox.petra.lang.math;

import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.util.function.IFunction;

import static io.cognitionbox.petra.lang.math.BigDecimalUtils.*;

public class IntMult<T extends IIntegerOp> extends PEdge<T> {
    public IntMult(IFunction<T,Integer> value)
    {
        type((Class<T>) IIntegerOp.class);
        kase(x -> x.result()>0 && value.apply(x)<0, x -> x.result()>0);
        kase(x -> x.result()>0 && value.apply(x)>0, x -> x.result()>0);
        kase(x -> x.result()>0 && value.apply(x)<0, x -> x.result()<0);
        kase(x -> x.result()<0 && value.apply(x)>0, x -> x.result()<0);
        kase(x -> x.result()==0, x -> x.result()==0);
        kase(x -> value.apply(x)==0, x -> x.result()==0);
        func(x ->{
            x.result(x.result()*value.apply(x));
        });
    }
}