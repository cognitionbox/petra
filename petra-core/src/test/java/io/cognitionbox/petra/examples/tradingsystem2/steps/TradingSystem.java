package io.cognitionbox.petra.examples.tradingsystem2.steps;

import io.cognitionbox.petra.examples.tradingsystem2.objects.State;
import io.cognitionbox.petra.examples.tradingsystem2.steps.factors.Reshuffle;
import io.cognitionbox.petra.examples.tradingsystem2.steps.marketdata.SelectHistoricalFeed;
import io.cognitionbox.petra.examples.tradingsystem2.steps.positions.CloseAllTraderDecisions;
import io.cognitionbox.petra.examples.tradingsystem2.steps.positions.RemoveDecisions;
import io.cognitionbox.petra.examples.tradingsystem2.steps.quant.CalculateAndSetBetaNeutralQuantities;
import io.cognitionbox.petra.examples.tradingsystem2.steps.quant.CalculateBeta;
import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.choice;

public class TradingSystem extends PGraph<State> {
    {
        type(State.class);
        //setSleepPeriod(1000);
        iterations(x->1097);
        pre(state -> !state.hasFeed());
        begin();
        init(state -> state, SelectHistoricalFeed.class);
        init(state -> state, StartFeed.class);
        init(state -> state, Reshuffle.class);
        init(state -> state, CalculateBeta.class);
        init(state -> state, CalculateAndSetBetaNeutralQuantities.class);
        init(state -> state, UpdateQuotes.class);
        steps(choice(), state -> state.getTraders(), CloseAllTraderDecisions.class);
        skip(choice());
        step(state -> state, DisplayPnl.class);
        steps(state -> state.getTraders(), RemoveDecisions.class);
        steps(state -> state.getTraders(), Trade.class);
        step(state -> state, UpdateQuotes.class);
        end();
        post(state -> true);
    }
}