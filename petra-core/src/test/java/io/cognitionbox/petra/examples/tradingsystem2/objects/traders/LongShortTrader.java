package io.cognitionbox.petra.examples.tradingsystem2.objects.traders;

import io.cognitionbox.petra.examples.tradingsystem2.objects.decisions.Direction;

public class LongShortTrader extends AbstractTrader {
    public LongShortTrader(String instrument, Direction direction) {
        super(instrument, direction);
    }
}
