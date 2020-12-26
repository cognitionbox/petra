package io.cognitionbox.petra.examples.kases.math;

import io.cognitionbox.petra.lang.PEdge;

import static io.cognitionbox.petra.examples.kases.math.R.*;

public class BiMult extends PEdge<BiMath> {
    {
        type(BiMath.class);
        kase(x -> isPos(x.a()) && isNeg(x.b()), x -> isPos(x.result()));
        kase(x -> isPos(x.a()) && isPos(x.b()), x -> isPos(x.result()));
        kase(x -> isPos(x.a()) && isNeg(x.b()), x -> isNeg(x.result()));
        kase(x -> isNeg(x.a()) && isPos(x.b()), x -> isNeg(x.result()));
        kase(x -> isZero(x.a()), x -> isZero(x.result()));
        kase(x -> isZero(x.b()), x -> isZero(x.result()));
        func(x ->{
            x.setResult(x.a().multiply(x.b()));
        });
    }
}