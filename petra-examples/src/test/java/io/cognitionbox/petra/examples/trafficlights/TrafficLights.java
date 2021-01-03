package io.cognitionbox.petra.examples.trafficlights;

import io.cognitionbox.petra.examples.trafficlights.objects.Lights;
import io.cognitionbox.petra.examples.trafficlights.steps.Go;
import io.cognitionbox.petra.examples.trafficlights.steps.Stop;
import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.seq;

public class TrafficLights extends PGraph<Lights> {
    {
        type(Lights.class);
        iterations(2);
        kase(lights -> lights.isRed() && !lights.isGreen(), lights -> lights.isGreen());
        kase(lights -> lights.isGreen() && lights.isRed(), lights -> lights.isRed() && !lights.isGreen());
        step(lights -> lights, new Go(),seq());
        step(lights -> lights, new Stop(),seq());
    }
}