package io.cognitionbox.petra.examples.tradingsystem2.objects;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AbstractFeed implements Feed {
    protected Map<String, Quote> currentQuote = new ConcurrentHashMap<>();

    @Override
    public void sink(Quote quote, String epic) {
        currentQuote.put(epic,quote);
    }

    @Override
    public Quote source(String epic) {
        return currentQuote.get(epic);
    }
}
