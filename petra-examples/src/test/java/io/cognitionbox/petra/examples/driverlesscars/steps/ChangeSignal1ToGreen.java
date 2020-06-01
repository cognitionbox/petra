package io.cognitionbox.petra.examples.driverlesscars.steps;

import io.cognitionbox.petra.examples.driverlesscars.SignalState;
import io.cognitionbox.petra.examples.driverlesscars.Simlulation;
import io.cognitionbox.petra.lang.PEdge;

public class ChangeSignal1ToGreen extends PEdge<Simlulation> {
    {
        pre(Simlulation.class, s->(s.signalAisRED() && s.signalBisGREEN()) && Math.random()>0.3);
        func(s->{
            s.getSignalA().setToGreenLight();
            s.getSignalB().setToRedLight();
            System.out.println("signal 1 = "+s.getSignalA().getSignal());
            System.out.println("signal 2 = "+s.getSignalB().getSignal());
            return s;
        });
        post(Simlulation.class, s->(s.signalAisGREEN() && s.signalBisRED()));
    }
}
