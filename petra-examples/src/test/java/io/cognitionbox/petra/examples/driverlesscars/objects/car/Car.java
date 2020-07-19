package io.cognitionbox.petra.examples.driverlesscars.objects.car;

import io.cognitionbox.petra.examples.driverlesscars.Signal;
import io.cognitionbox.petra.examples.driverlesscars.SignalState;
import io.cognitionbox.petra.examples.driverlesscars.permissions.ReadSignal;

import java.io.Serializable;

public class Car implements ReadSignal, Serializable {
    private Signal trafficLight;

    public Car(Signal trafficLight) {
        this.trafficLight = trafficLight;
    }

    @Override
    public SignalState getSignal() {
        return this.trafficLight.getSignal();
    }
}
