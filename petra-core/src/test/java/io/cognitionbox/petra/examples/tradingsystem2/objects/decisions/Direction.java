package io.cognitionbox.petra.examples.tradingsystem2.objects.decisions;

public enum Direction {
    LONG,
    SHORT;

    public boolean LONG() {
        return this == LONG;
    }

    public boolean SHORT() {
        return this == SHORT;
    }
}