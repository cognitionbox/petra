package io.cognitionbox.petra.examples.kases4.steps;

import io.cognitionbox.petra.examples.kases4.objects.SystemState;
import io.cognitionbox.petra.lang.PGraph;

public class SystemGraph extends PGraph<SystemState> {
    {
        type(SystemState.class);
        kase(s->s.getState()==SystemState.State.A,s->s.getState()==SystemState.State.B);
        kase(s->s.getState()==SystemState.State.B,s->s.getState()==SystemState.State.C);
        kase(s->s.getState()==SystemState.State.C,s->s.getState()==SystemState.State.D);
        step(s->s,new SystemEdge());
    }
}
