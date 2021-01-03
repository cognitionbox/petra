package io.cognitionbox.petra.examples.kases2.math;

import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.math.Nullary;

import java.math.BigDecimal;

public class Add extends PEdge<Nullary> {
    public Add(int b){
        this(new BigDecimal(b));
    }
    public Add(double b){
        this(new BigDecimal(b));
    }
    public Add(BigDecimal b)
    {
        type(Nullary.class);
        kase(x -> true, x -> true);
        func(x ->{
            x.result(x.result().add(b));
        });
    }
}