package io.cognitionbox.petra.examples.tradingsystem2.objects.marketdata;

import org.ta4j.core.num.Num;

import java.io.Serializable;

public class QuoteImp implements Quote, Serializable {
    private Num bid;
    private Num ask;
    private Num mid;
    private Num spread;

    public QuoteImp(Num bid, Num ask, Num mid, Num spread) {
        this.bid = bid;
        this.ask = ask;
        this.mid = mid;
        this.spread = spread;
    }

    @Override
    public Num getBid() {
        return bid;
    }

    @Override
    public Num getAsk() {
        return ask;
    }

    @Override
    public Num getMid() {
        return mid;
    }

    @Override
    public Num getSpread() {
        return spread;
    }
}
