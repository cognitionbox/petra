package io.cognitionbox.petra.lang.impls.flashinglights.objects;

import io.cognitionbox.petra.lang.Ref;
import static io.cognitionbox.petra.util.Petra.ref;


public class Lights {
    public enum Colour {
        RED,
        GREEN,
        BLUE
    }
    public final Ref<Colour> colour = ref(Colour.RED);
}
