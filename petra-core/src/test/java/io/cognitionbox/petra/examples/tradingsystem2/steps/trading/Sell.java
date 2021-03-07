package io.cognitionbox.petra.examples.tradingsystem2.steps.trading;

import io.cognitionbox.petra.examples.tradingsystem2.objects.decisions.DecisionImp;
import io.cognitionbox.petra.examples.tradingsystem2.objects.decisions.Direction;
import io.cognitionbox.petra.examples.tradingsystem2.objects.traders.Trader;
import io.cognitionbox.petra.lang.PEdge;

public class Sell extends PEdge<Trader> {
    {
        type(Trader.class);
        pre(trader -> trader.getDirection() == Direction.SHORT);
        func(trader -> {
            trader.makeDecision(new DecisionImp(Direction.SHORT, trader.getEntry(), trader.getBetaNeutralQty()));
        });
        post(trader -> trader.getLastDecision().getDirection().SHORT() &&
                trader.getCurrentQuote().getBid().equals(trader.getLastDecision().getEntry()));
    }
}
