package io.cognitionbox.petra.lang.impls.kases4.steps;

import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.impls.kases4.objects.SystemState;

import static io.cognitionbox.petra.util.Petra.seq;

public class SystemGraph extends PGraph<SystemState> {
    {
        type(SystemState.class);
        kase(s->s.getState()==SystemState.State.A,s->s.getState()==SystemState.State.B);
        kase(s->s.getState()==SystemState.State.B,s->s.getState()==SystemState.State.C);
        kase(s->s.getState()==SystemState.State.C,s->s.getState()==SystemState.State.D);
        begin();
        step(seq(),s->s,new SystemEdge());
        end();
    }
}
