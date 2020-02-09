package io.cognitionbox.petra.examples.tradingsystem.objects;

public enum InstrumentId {
    FTSE("FTSE"),
    DAX("DAX");
    private String id;
    InstrumentId(String id) {
        this.id = id;
    }
}
