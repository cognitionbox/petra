package io.cognitionbox.petra.examples.driverlesscars;

import io.cognitionbox.petra.examples.driverlesscars.permissions.ReadSignal;
import io.cognitionbox.petra.examples.driverlesscars.permissions.WriteSignal;

import java.io.Serializable;

public class Signal implements ReadSignal, WriteSignal, Serializable {
    public Signal(SignalState state) {
        this.state = state;
    }

    public SignalState getSignal() {
        return state;
    }

    public void setToGreenLight() {
        this.state = SignalState.GREEN;
    }

    public void setToRedLight() {
        this.state = SignalState.RED;
    }

    private SignalState state;

    @Override
    public String toString() {
        return "Signal{" +
                "state=" + state +
                '}';
    }
}
