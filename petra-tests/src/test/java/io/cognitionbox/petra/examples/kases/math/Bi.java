package io.cognitionbox.petra.examples.kases.math;

import java.math.BigDecimal;

public class Bi extends Un implements BiMath {
    public Bi(BigDecimal a, BigDecimal b, BigDecimal result) {
        super(a,result);
        this.b = b;
    }

    private BigDecimal b;

    public BigDecimal b() {
        return b;
    }

    public void b(BigDecimal b) {
        this.b = b;
    }
}
