package io.cognitionbox.petra.lang.impls.flashinglights.steps;

import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.impls.flashinglights.objects.Lights;

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
