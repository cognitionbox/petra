package io.cognitionbox.petra.lang.impls.kases4.steps;

import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.impls.kases4.objects.SystemState;

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
