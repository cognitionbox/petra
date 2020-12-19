package io.cognitionbox.petra.examples.trafficlights.steps;

import io.cognitionbox.petra.examples.trafficlights.objects.Lights;
import io.cognitionbox.petra.lang.PEdge;

public class Go extends PEdge<Lights> {
    {
        type(Lights.class);
        pre(lights -> lights.isRed() && !lights.isGreen());
        func(lights -> {
            lights.setGreen(true);
        });
        post(lights -> lights.isGreen());
    }
}
