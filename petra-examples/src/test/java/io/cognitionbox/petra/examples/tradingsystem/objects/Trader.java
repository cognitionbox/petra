package io.cognitionbox.petra.examples.tradingsystem.objects;

import io.cognitionbox.petra.examples.tradingsystem.steps.marketdata.TraderOk;

import java.io.Serializable;


public interface Trader extends Serializable, TraderOk {
    TraderId id();

    InstrumentId getInstrument();

    Decisions runStrategy(Tick tick);

    void setFeed(Feed feed);
    Feed getFeed();

    enum Direction {
        BUY,
        SELL
    }
}