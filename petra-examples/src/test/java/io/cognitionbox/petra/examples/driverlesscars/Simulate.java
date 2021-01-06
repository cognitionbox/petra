package io.cognitionbox.petra.examples.driverlesscars;

import io.cognitionbox.petra.examples.driverlesscars.steps.ChangeSignal1ToGreen;
import io.cognitionbox.petra.examples.driverlesscars.steps.ChangeSignal2ToGreen;
import io.cognitionbox.petra.examples.driverlesscars.steps.MoveCarIntoJunction;
import io.cognitionbox.petra.examples.driverlesscars.steps.MoveCarOutOfJunction;
import io.cognitionbox.petra.lang.PGraph;

public class Simulate extends PGraph<Simlulation> {
    {
        type(Simlulation.class);
        iterations(x->10);
        //setSleepPeriod(1000);
        //        gi(s->(s.getNoOfCarsInJunction()<=1 &&
//                ((s.signalAisRED() && s.signalBisGREEN()) ^
//                        (s.signalAisGREEN() && s.signalBisRED())) ) );
        pre(s->
                (s.getNoOfCarsInJunction()<=1 &&
                ((s.signalAisRED() && s.signalBisGREEN()) ^
                        (s.signalAisGREEN() && s.signalBisRED())) ) );
        step(new ChangeSignal1ToGreen());
        step(new ChangeSignal2ToGreen());
        step(new MoveCarIntoJunction());
        step(new MoveCarOutOfJunction());
        post(c->true);
    }
}
