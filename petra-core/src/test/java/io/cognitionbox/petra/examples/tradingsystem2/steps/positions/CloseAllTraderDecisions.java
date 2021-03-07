package io.cognitionbox.petra.examples.tradingsystem2.steps.positions;

import io.cognitionbox.petra.examples.tradingsystem2.objects.traders.Trader;
import io.cognitionbox.petra.examples.tradingsystem2.steps.trading.UpdateDecisionQuotes;
import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.choice;

public class CloseAllTraderDecisions extends PGraph<Trader> {
    {
        type(Trader.class);
        pre(trader -> !trader.getDecisions().isEmpty());
        begin();
        step(trader -> trader, UpdateDecisionQuotes.class);
        steps(choice(), trader -> trader.getDecisions(), CloseBuyDecisionEOD.class);
        steps(choice(), trader -> trader.getDecisions(), CloseSellDecisionEOD.class);
        step(trader -> trader, AggTraderPnl.class);
        end();
        post(trader -> true);
    }
}
