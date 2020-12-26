package io.cognitionbox.petra.examples.kases.math;

import java.math.BigDecimal;

public class Un implements UnMath {
    public Un(BigDecimal a, BigDecimal result) {
        this.a = a;
        this.result = result;
    }

    private BigDecimal a;
    private BigDecimal result;

    public BigDecimal a() {
        return a;
    }

    public void a(BigDecimal a) {
        this.a = a;
    }

    @Override
    public BigDecimal result() {
        return result;
    }

    @Override
    public void setResult(BigDecimal result) {
        this.result = result;
    }
}
