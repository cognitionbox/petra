package io.cognitionbox.petra.examples.tradingsystem2.objects.decisions;

import io.cognitionbox.petra.examples.tradingsystem2.objects.marketdata.Quote;
import org.ta4j.core.num.Num;

public interface Decision {
    Quote getCurrentQuote();

    void setCurrentQuote(Quote quote);

    Status getStatus();

    void setStatus(Status status);

    Direction getDirection();

    Num getEntry();

    Num getClosedPnl();

    void setClosedPnl(Num closedPnl);

    Num getQuantity();
}
