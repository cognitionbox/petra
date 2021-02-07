package io.cognitionbox.petra.examples.tradingsystem2.objects;

public interface Feed {
    void sink(Quote quote, String epic); // puts into feed
    Quote source(String epic); // reads from feed
}
