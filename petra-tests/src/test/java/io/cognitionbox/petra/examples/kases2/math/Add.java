package io.cognitionbox.petra.examples.kases2.math;

import io.cognitionbox.petra.lang.PEdge;

import java.math.BigDecimal;

import static io.cognitionbox.petra.examples.kases.math.R.*;

public class Add extends PEdge<IMath> {
    public Add(int b){
        this(new BigDecimal(b));
    }
    public Add(double b){
        this(new BigDecimal(b));
    }
    public Add(BigDecimal b)
    {
        type(IMath.class);
        kase(x -> true, x -> true);
        func(x ->{
            x.result(x.result().add(b));
        });
    }
}