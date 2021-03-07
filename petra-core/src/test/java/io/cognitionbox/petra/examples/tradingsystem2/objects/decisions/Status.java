package io.cognitionbox.petra.examples.tradingsystem2.objects.decisions;

public enum Status {
    NEW,
    OPENED,
    CLOSED;

    public boolean isNEW() {
        return this == NEW;
    }

    public boolean isOPENED() {
        return this == OPENED;
    }

    public boolean isCLOSED() {
        return this == CLOSED;
    }
}