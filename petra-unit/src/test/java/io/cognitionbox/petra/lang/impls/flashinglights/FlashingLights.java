package io.cognitionbox.petra.lang.impls.flashinglights;

import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.impls.flashinglights.objects.Lights;
import io.cognitionbox.petra.lang.impls.flashinglights.steps.ChangeColour;

import static io.cognitionbox.petra.util.Petra.seq;

public class FlashingLights extends PGraph<Lights> {
    {
        type(Lights.class);
        iterations(x->3);
        variant((i,x)->x.colour.get()==Lights.Colour.values()[i]);
        kase(lights -> lights.colour.get()== Lights.Colour.RED, lights -> lights.colour.get()== Lights.Colour.RED);

        step(seq(),lights -> lights, new ChangeColour());
        esak();
    }
}