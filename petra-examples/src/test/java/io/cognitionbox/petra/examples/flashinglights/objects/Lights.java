package io.cognitionbox.petra.examples.flashinglights.objects;

import io.cognitionbox.petra.lang.RW;
import io.cognitionbox.petra.util.Petra;

import static io.cognitionbox.petra.util.Petra.rw;

public class Lights {
    public enum Colour {
        RED,
        GREEN,
        BLUE
    }
    public final RW<Colour> colour = rw(Colour.RED);
}
