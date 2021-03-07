package io.cognitionbox.petra.examples.tradingsystem2.steps.quant;

import io.cognitionbox.petra.examples.tradingsystem2.objects.traders.Trader;
import io.cognitionbox.petra.lang.PEdge;

public class CalcBetaForTrader extends PEdge<Trader> {
    {
        type(Trader.class);
        pre(t -> t.getBeta() == null && t.getVariance() != null && t.getCovariance() != null);
        func(t -> t.setBeta(t.getCovariance().dividedBy(t.getVariance())));
        post(t -> t.getBeta() != null);
    }
}
