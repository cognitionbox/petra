package io.cognitionbox.petra.examples.tradingsystem.objects;

import java.io.Serializable;


public interface Trader extends Serializable {

    Decisions getDecisions();

    void addDecision(Decision d);

    Decisions runStrategy(Tick tick);

    void setFeed(Feed feed);

    Feed getFeed();

    boolean hasFeed();

    boolean hasGtZeroDecisions();

    boolean hasEqZeroDecisions();

    enum Direction {
        BUY,
        SELL
    }
}