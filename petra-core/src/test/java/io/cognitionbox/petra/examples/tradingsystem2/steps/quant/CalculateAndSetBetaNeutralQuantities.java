package io.cognitionbox.petra.examples.tradingsystem2.steps.quant;

import io.cognitionbox.petra.examples.tradingsystem2.objects.State;
import io.cognitionbox.petra.examples.tradingsystem2.objects.traders.Trader;
import io.cognitionbox.petra.lang.PEdge;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.PrecisionNum;

import static io.cognitionbox.petra.util.Petra.forAll;

// calculation needs reviewing
public class CalculateAndSetBetaNeutralQuantities extends PEdge<State> {
    {
        type(State.class);
        pre(state -> forAll(Trader.class, state.getTraders(), t -> t.getBetaNeutralQty() == null));
        func(state -> {
            Num q2 = PrecisionNum.valueOf(1);
            Num p2 = state.getHistoricalFeed().getQuote(state.getChosenLongShortPair().getToShort()).getMid();

            Num p1 = state.getHistoricalFeed().getQuote(state.getChosenLongShortPair().getToLong()).getMid();

            Num q1 = q2.multipliedBy(p2)
                    .multipliedBy(state.getTrader(state.getChosenLongShortPair().getToShort()).getBeta())
                    .dividedBy(state.getTrader(state.getChosenLongShortPair().getToLong()).getBeta())
                    .dividedBy(p1);

            state.getTrader(state.getChosenLongShortPair().getToShort()).setBetaNeutralQty(q2);
            state.getTrader(state.getChosenLongShortPair().getToLong()).setBetaNeutralQty(q1);
        });
        post(state -> forAll(Trader.class, state.getTraders(), t -> t.getBetaNeutralQty() != null));
    }
}
