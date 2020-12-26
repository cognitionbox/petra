package io.cognitionbox.petra.examples.kases.math;

import io.cognitionbox.petra.lang.PEdge;

import java.math.BigDecimal;

import static io.cognitionbox.petra.examples.kases.math.R.*;

public class AssignUn extends PEdge<UnMath> {
    {
        type(UnMath.class);
        kase(x ->notNull(x.result()), x -> x.a().equals(x.result()));
        func(x ->{
            x.a(x.result());
        });
    }
}