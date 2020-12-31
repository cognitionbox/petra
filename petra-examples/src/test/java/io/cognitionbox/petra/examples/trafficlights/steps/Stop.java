package io.cognitionbox.petra.examples.trafficlights.steps;

import io.cognitionbox.petra.examples.trafficlights.objects.Lights;
import io.cognitionbox.petra.lang.PEdge;

public class Stop extends PEdge<Lights> {
    {
        type(Lights.class);
        pre(lights -> lights.isGreen());
        func(lights -> {
            lights.setRed(true);
            lights.setGreen(false);
        });
        post(lights -> lights.isRed());
    }
}
