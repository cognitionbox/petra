package io.cognitionbox.petra.examples.tradingsystem2.objects;

import java.math.BigDecimal;

public interface Quote {
    BigDecimal getBid();
    BigDecimal getAsk();
}
