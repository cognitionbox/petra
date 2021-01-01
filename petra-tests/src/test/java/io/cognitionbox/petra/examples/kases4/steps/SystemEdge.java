package io.cognitionbox.petra.examples.kases4.steps;

import io.cognitionbox.petra.examples.kases4.objects.SystemState;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.PGraph;

public class SystemEdge extends PEdge<SystemState> {
    {
        type(SystemState.class);
        kase(s->s.getState()==SystemState.State.A,s->s.getState()==SystemState.State.B);
        kase(s->s.getState()==SystemState.State.B,s->s.getState()==SystemState.State.C);
        kase(s->s.getState()==SystemState.State.C,s->s.getState()==SystemState.State.D);
        func(s->{
           s.setState(SystemState.State.values()[s.getState().ordinal()+1]);
        });
    }
}
