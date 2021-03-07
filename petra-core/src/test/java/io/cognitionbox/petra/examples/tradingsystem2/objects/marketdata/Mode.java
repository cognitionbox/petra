package io.cognitionbox.petra.examples.tradingsystem2.objects.marketdata;

public enum Mode {
    LIVE,
    HISTORICAL;

    public boolean isLIVE() {
        return this == LIVE;
    }

    public boolean isHISTORICAL() {
        return this == HISTORICAL;
    }
}