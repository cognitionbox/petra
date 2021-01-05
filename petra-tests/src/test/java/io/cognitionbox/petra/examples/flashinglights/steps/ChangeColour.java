package io.cognitionbox.petra.examples.flashinglights.steps;

import io.cognitionbox.petra.examples.flashinglights.objects.Lights;
import io.cognitionbox.petra.lang.PEdge;

public class ChangeColour extends PEdge<Lights> {
    {
        type(Lights.class);
        kase(lights -> lights.colour.get()== Lights.Colour.RED, lights -> lights.colour.get()== Lights.Colour.GREEN);
        kase(lights -> lights.colour.get()== Lights.Colour.GREEN, lights -> lights.colour.get()== Lights.Colour.BLUE);
        kase(lights -> lights.colour.get()== Lights.Colour.BLUE, lights -> lights.colour.get()== Lights.Colour.RED);
        func(lights -> {
            lights.colour.set(Lights.Colour.values()[(lights.colour.get().ordinal()+1)%3]);
        });
    }
}
