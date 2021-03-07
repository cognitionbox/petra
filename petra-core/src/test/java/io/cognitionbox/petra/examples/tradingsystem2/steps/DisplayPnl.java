package io.cognitionbox.petra.examples.tradingsystem2.steps;

import io.cognitionbox.petra.examples.tradingsystem2.objects.State;
import io.cognitionbox.petra.lang.PEdge;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.PrecisionNum;

public class DisplayPnl extends PEdge<State> {
    {
        type(State.class);
        pre(state -> true);
        func(state -> {
            Num pnl = state.getTraders().stream().map(t -> t.getClosedPnl()).reduce(PrecisionNum.valueOf(0), Num::plus);
            state.setClosedPnl(pnl);
            System.out.println("Total Pnl: £" + pnl);
            System.out.println("Long Pnl: £" + state.getTrader(state.getChosenLongShortPair().getToLong()).getClosedPnl());
            System.out.println("Short Pnl: £" + state.getTrader(state.getChosenLongShortPair().getToShort()).getClosedPnl());
        });
        post(state -> true);
    }
}