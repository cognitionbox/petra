package io.cognitionbox.petra.examples.tradingsystem2.steps.quant;

import io.cognitionbox.petra.examples.tradingsystem2.objects.State;
import io.cognitionbox.petra.examples.tradingsystem2.objects.traders.Trader;
import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.forAll;

/*
 * Concurrency safety needs thought...
 * SetTradersWithQuoteAndBenchmarkDiffs has the potential to pass a reference from the State instance into
 * each Trader within the State instance, and the following stepForall would allow the Trader's
 * to be operated on in parallel, causing a race condition.
 * This is not the case here however the potential is present, the question is does the current theory of
 * reference reachability (which has maily been considered for back-references) work in this scenario?
 */
public class CalculateBeta extends PGraph<State> {
    {
        type(State.class);
        pre(state -> forAll(Trader.class, state.getTraders(), t -> t.getBeta() == null));
        begin();
        step(state -> state, SetTradersWithQuoteAndBenchmarkDiffs.class);
        steps(state -> state.getTraders(), CalcVarianceForTrader.class);
        steps(state -> state.getTraders(), CalcCovarianceForTrader.class);
        steps(state -> state.getTraders(), CalcBetaForTrader.class);
        end();
        post(state -> forAll(Trader.class, state.getTraders(), t -> t.getBeta() != null));
    }
}
