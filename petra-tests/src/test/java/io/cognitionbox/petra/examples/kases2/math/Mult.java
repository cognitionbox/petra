package io.cognitionbox.petra.examples.kases2.math;

import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.math.IBigDecimalOp;

import java.math.BigDecimal;

import static io.cognitionbox.petra.examples.kases.math.R.*;

public class Mult extends PEdge<IBigDecimalOp> {
    public Mult(int b){
        this(new BigDecimal(b));
    }
    public Mult(double b){
        this(new BigDecimal(b));
    }
    public Mult(BigDecimal b)
    {
        type(IBigDecimalOp.class);
        kase(x -> isPos(x.result()) && isNeg(b), x -> isPos(x.result()));
        kase(x -> isPos(x.result()) && isPos(b), x -> isPos(x.result()));
        kase(x -> isPos(x.result()) && isNeg(b), x -> isNeg(x.result()));
        kase(x -> isNeg(x.result()) && isPos(b), x -> isNeg(x.result()));
        kase(x -> isZero(x.result()), x -> isZero(x.result()));
        kase(x -> isZero(b), x -> isZero(x.result()));
        func(x ->{
            x.result(x.result().multiply(b));
        });
    }
}