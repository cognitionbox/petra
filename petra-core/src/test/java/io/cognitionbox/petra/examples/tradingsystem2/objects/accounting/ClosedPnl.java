package io.cognitionbox.petra.examples.tradingsystem2.objects.accounting;

import org.ta4j.core.num.Num;

/*
 * Idea for prevent mistakes in orderings of arguments, wrap types around numerical values.
 */
public final class ClosedPnl {
    private final Num closedPnl;

    public ClosedPnl(Num closedPnl) {
        this.closedPnl = closedPnl;
    }

    public Num getClosedPnl() {
        return closedPnl;
    }
}
