package io.cognitionbox.petra.lang.math;


import java.math.BigDecimal;

public interface Nullary extends IOp<BigDecimal> {
    BigDecimal result();
    void result(BigDecimal result);
}
