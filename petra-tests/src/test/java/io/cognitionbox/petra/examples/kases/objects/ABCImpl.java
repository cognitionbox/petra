package io.cognitionbox.petra.examples.kases.objects;

import io.cognitionbox.petra.examples.kases.math.*;

import java.math.BigDecimal;

public class ABCImpl implements ABC {
    private BigDecimal a = BigDecimal.ONE.add(BigDecimal.ONE);
    private BigDecimal b;
    private BigDecimal c;

    public BigDecimal a() {
        return a;
    }

    public void a(BigDecimal a) {
        this.a = a;
    }

    public BigDecimal b() {
        return b;
    }

    public void b(BigDecimal b) {
        this.b = b;
    }

    public BigDecimal result() {
        return c;
    }

    public void setResult(BigDecimal c) {
        this.c = c;
    }

    @Override
    public UnMath toUn() {
        return this;
    }

    @Override
    public BiMath toBi() {
        return this;
    }

    @Override
    public String toString() {
        return "ABCImpl{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                '}';
    }
}
