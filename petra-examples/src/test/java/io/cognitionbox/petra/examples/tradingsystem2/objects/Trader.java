package io.cognitionbox.petra.examples.tradingsystem2.objects;

import java.util.List;

public interface Trader {
    void runStrategy(Quote quote); // creates decisions
    List<Decision> getDecisions();
}
