package io.cognitionbox.petra.examples.tradingsystem2.objects.decisions;

import io.cognitionbox.petra.examples.tradingsystem2.objects.marketdata.Quote;
import org.ta4j.core.num.Num;

public class DecisionImp implements Decision {
    private final Num entry;
    private final Num quantity;
    private final Direction direction;
    private Status status = Status.NEW;
    private Quote quote = null;
    private Num closedPnl;

    public DecisionImp(Direction direction, Num entry, Num quantity) {
        this.direction = direction;
        this.entry = entry;
        this.quantity = quantity;
    }

    @Override
    public Quote getCurrentQuote() {
        return quote;
    }

    @Override
    public void setCurrentQuote(Quote quote) {
        this.quote = quote;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public Num getEntry() {
        return entry;
    }

    @Override
    public Num getClosedPnl() {
        return closedPnl;
    }

    @Override
    public void setClosedPnl(Num closedPnl) {
        this.closedPnl = closedPnl;
    }

    @Override
    public Num getQuantity() {
        return quantity;
    }
}
