package io.cognitionbox.petra.examples.driverlesscars;

public enum SignalState {
    RED,
    GREEN;

    public boolean isRED(){
        return this==SignalState.RED;
    }

    public boolean isGREEN(){
        return this==SignalState.GREEN;
    }
}
