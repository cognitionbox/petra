package io.cognitionbox.petra.examples.tradingsystem2.steps.factors;

import io.cognitionbox.petra.examples.tradingsystem2.objects.State;
import io.cognitionbox.petra.examples.tradingsystem2.objects.decisions.Direction;
import io.cognitionbox.petra.examples.tradingsystem2.objects.factors.ESGLongShortPair;
import io.cognitionbox.petra.examples.tradingsystem2.objects.traders.Trader;
import io.cognitionbox.petra.lang.PCollectionEdge;

import static io.cognitionbox.petra.util.Petra.seq;

public class UpdateLongTraders extends PCollectionEdge<State, ESGLongShortPair, Trader> {
    {
        type(State.class);
        collection(state -> state.getTraders());
        shared(seq(),state -> state.getChosenLongShortPair());
        pre((esg,trader) ->
                esg.getToLong()!=null &&
                esg.getToShort()!=null &&
                        trader.getDirection()==Direction.LONG &&
                        !trader.getInstrument().equals(esg.getToLong()));
        func(
                (esg, trader) -> {
                    trader.setClosePositionsEOD(true);
                }
        );
        post((feed, trader) -> true);
    }
}
