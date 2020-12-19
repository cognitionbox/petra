package io.cognitionbox.petra.examples.trafficlights;

import io.cognitionbox.petra.examples.trafficlights.objects.Lights;
import io.cognitionbox.petra.examples.trafficlights.steps.Go;
import io.cognitionbox.petra.examples.trafficlights.steps.Stop;
import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.seq;

public class TrafficLights extends PGraph<Lights> {
    {
        type(Lights.class);
        pre(lights -> lights.isRed() && !lights.isGreen());
        step(lights -> lights, new Go(),seq());
        step(lights -> lights, new Stop(),seq());
        post(lights -> lights.isRed() && !lights.isGreen());
    }
}