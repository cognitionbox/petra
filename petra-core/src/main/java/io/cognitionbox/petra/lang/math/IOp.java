package io.cognitionbox.petra.lang.math;

import java.math.BigDecimal;

public interface IOp<T> {
    T result();
    void result(T result);
}
