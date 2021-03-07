package io.cognitionbox.petra.examples.tradingsystem2.steps;

import io.cognitionbox.petra.examples.tradingsystem2.objects.traders.Trader;
import io.cognitionbox.petra.examples.tradingsystem2.steps.positions.OpenDecision;
import io.cognitionbox.petra.examples.tradingsystem2.steps.trading.Buy;
import io.cognitionbox.petra.examples.tradingsystem2.steps.trading.Sell;
import io.cognitionbox.petra.examples.tradingsystem2.steps.trading.SetEntryAsMid;
import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.choice;

public class Trade extends PGraph<Trader> {
    {
        type(Trader.class);
        pre(trader -> trader.hasQuote());
        begin();
        step(trader -> trader, SetEntryAsMid.class);
        step(choice(), trader -> trader, Buy.class);
        step(choice(), trader -> trader, Sell.class);
        steps(trader -> trader.getDecisions(), OpenDecision.class);
        end();
        post(trader -> true);
    }
}
