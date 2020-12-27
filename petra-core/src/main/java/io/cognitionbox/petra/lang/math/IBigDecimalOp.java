package io.cognitionbox.petra.lang.math;


import java.math.BigDecimal;

public interface IBigDecimalOp extends IOp<BigDecimal> {
    BigDecimal result();
    void result(BigDecimal result);
}
