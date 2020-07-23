package io.cognitionbox.petra.examples.driverlesscars.steps;

import io.cognitionbox.petra.examples.driverlesscars.Simlulation;
import io.cognitionbox.petra.lang.PEdge;

public class ChangeSignal2ToGreen extends PEdge<Simlulation> {
    {
        type(Simlulation.class);
        preC(s->(s.signalBisRED() && s.signalAisGREEN()) && Math.random()>0.3);
        func(s->{
            s.getSignalB().setToGreenLight();
            s.getSignalA().setToRedLight();
            System.out.println("signal 1 = "+s.getSignalA().getSignal());
            System.out.println("signal 2 = "+s.getSignalB().getSignal());
            return s;
        });
        postC(s->(s.signalBisGREEN() && s.signalAisRED()));
    }
}
