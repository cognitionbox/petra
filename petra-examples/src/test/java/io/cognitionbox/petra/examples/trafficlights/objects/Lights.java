package io.cognitionbox.petra.examples.trafficlights.objects;

import io.cognitionbox.petra.lang.Ref;

import static io.cognitionbox.petra.util.Petra.ref;

public class Lights {
    private final Ref<Boolean> red = ref(true);
    private final Ref<Boolean> green = ref(false);

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
