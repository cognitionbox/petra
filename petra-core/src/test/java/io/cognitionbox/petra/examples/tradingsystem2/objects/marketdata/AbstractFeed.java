package io.cognitionbox.petra.examples.tradingsystem2.objects.marketdata;

import org.ta4j.core.BarSeries;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractFeed implements Feed {
    protected Map<String, BarSeries> barSeriesMap = new ConcurrentHashMap<>();

    @Override
    public BarSeries getBarSeriesForInstrument(String instrument) {
        return barSeriesMap.get(instrument);
    }

}
