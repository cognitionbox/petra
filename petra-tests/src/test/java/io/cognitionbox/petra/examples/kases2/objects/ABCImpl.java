package io.cognitionbox.petra.examples.kases2.objects;

import io.cognitionbox.petra.examples.kases2.math.Unary;

import java.math.BigDecimal;

public class ABCImpl implements ABC {
    private BigDecimal a = BigDecimal.ONE.add(BigDecimal.ONE);
    private BigDecimal c;

    public BigDecimal a() {
        return a;
    }

    public void a(BigDecimal a) {
        this.a = a;
    }

    public BigDecimal result() {
        return c;
    }

    public void result(BigDecimal c) {
        this.c = c;
    }

    @Override
    public Unary toUn() {
        return this;
    }

    @Override
    public String toString() {
        return "ABCImpl{" +
                "a=" + a +
                ", c=" + c +
                '}';
    }
}
