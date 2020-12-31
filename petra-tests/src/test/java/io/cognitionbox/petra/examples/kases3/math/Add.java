package io.cognitionbox.petra.examples.kases3.math;

import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.math.IBigDecimalOp;

import java.math.BigDecimal;

public class Add extends PEdge<IBigDecimalOp> {
    public Add(int b){
        this(new BigDecimal(b));
    }
    public Add(double b){
        this(new BigDecimal(b));
    }
    public Add(BigDecimal b)
    {
        type(IBigDecimalOp.class);
        kase(x -> true, x -> true);
        func(x ->{
            x.result(x.result().add(b));
        });
    }
}