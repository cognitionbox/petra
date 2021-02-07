package io.cognitionbox.petra.examples.tradingsystem2.objects;

import java.math.BigDecimal;

public interface Decision {
    public enum Status {
        NEW,
        OPENED,
        CLOSED
    }
    public enum Direction {
        BUY,
        SELL
    }
    Decision getStatus();
    Decision getDirection();
    BigDecimal getEntry();
    BigDecimal getExit();
}
