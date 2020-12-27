package io.cognitionbox.petra.examples.trafficlights.objects;

import io.cognitionbox.petra.lang.RW;
import io.cognitionbox.petra.util.Petra;

import static io.cognitionbox.petra.util.Petra.rw;

public class Lights {
    private final RW<Boolean> red = Petra.rw(true);
    private final RW<Boolean> green = Petra.rw(false);

    public boolean isRed() {
        return red.get();
    }

    public void setRed(boolean value) {
        this.red.set(value);
    }

    public boolean isGreen() {
        return green.get();
    }

    public void setGreen(boolean value) {
        this.green.set(value);
    }
}
