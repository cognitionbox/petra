package io.cognitionbox.petra.examples.flashinglights2.steps;

import io.cognitionbox.petra.examples.flashinglights2.objects.Lights;
import io.cognitionbox.petra.lang.PEdge;

public class ChangeColour extends PEdge<Lights> {
    {
        type(Lights.class);
        kase(lights -> lights.colour.get()== Lights.Colour.RED, lights -> lights.colour.get()== Lights.Colour.GREEN);
        // allows the light to move to red before blue
        kase(lights -> lights.colour.get()== Lights.Colour.GREEN, lights -> lights.colour.get()== Lights.Colour.RED);
        kase(lights -> lights.colour.get()== Lights.Colour.BLUE, lights -> lights.colour.get()== Lights.Colour.RED);
        func(lights -> {
            // if/else is used here just for testing to move the light back to red, instead of going to blue, ie
            // it by passes a kase
            if (lights.colour.get()==Lights.Colour.GREEN){
                lights.colour.set(Lights.Colour.RED);
            } else {
                lights.colour.set(Lights.Colour.values()[(lights.colour.get().ordinal()+1)%3]);
            }
            System.out.println(lights.colour.get());
        });
    }
}
