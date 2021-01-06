package io.cognitionbox.petra.examples.flashinglights;

import io.cognitionbox.petra.examples.flashinglights.objects.Lights;
import io.cognitionbox.petra.examples.flashinglights.steps.ChangeColour;
import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.seq;

public class FlashingLights extends PGraph<Lights> {
    {
        //infinite();
        //setSleepPeriod(1000);
        type(Lights.class);
        iterations(x->3);
        kase(lights -> lights.colour.get()== Lights.Colour.RED, lights -> lights.colour.get()== Lights.Colour.GREEN);
        kase(lights -> lights.colour.get()== Lights.Colour.GREEN, lights -> lights.colour.get()== Lights.Colour.BLUE);
        kase(lights -> lights.colour.get()== Lights.Colour.BLUE, lights -> lights.colour.get()== Lights.Colour.RED);
        step(lights -> lights, new ChangeColour(),seq());
    }
}