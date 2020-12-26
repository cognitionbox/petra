package io.cognitionbox.petra.examples.kases.math;

import io.cognitionbox.petra.lang.PEdge;

import java.math.BigDecimal;

import static io.cognitionbox.petra.examples.kases.math.R.*;
import static io.cognitionbox.petra.examples.kases.math.R.isZero;

public class Mult extends PEdge<UnMath> {
    public Mult(BigDecimal b)
    {
        type(UnMath.class);
        kase(x -> isPos(x.a()) && isNeg(b), x -> isPos(x.result()));
        kase(x -> isPos(x.a()) && isPos(b), x -> isPos(x.result()));
        kase(x -> isPos(x.a()) && isNeg(b), x -> isNeg(x.result()));
        kase(x -> isNeg(x.a()) && isPos(b), x -> isNeg(x.result()));
        kase(x -> isZero(x.a()), x -> isZero(x.result()));
        kase(x -> isZero(b), x -> isZero(x.result()));
        func(x ->{
            x.setResult(x.a().multiply(b));
        });
    }
}