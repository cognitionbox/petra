package io.cognitionbox.petra.examples.tradingsystem2.objects.marketdata;

import org.ta4j.core.BarSeries;

public interface Feed {
    boolean isStarted(String instrument);

    void start(String instrument);

    BarSeries getBarSeriesForInstrument(String instrument);

    Quote getQuote(String instrument);

    void addQuote(String instrument, Quote quote);
}
