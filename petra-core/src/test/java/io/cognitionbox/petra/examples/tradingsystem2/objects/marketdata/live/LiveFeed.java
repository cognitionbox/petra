package io.cognitionbox.petra.examples.tradingsystem2.objects.marketdata.live;

import io.cognitionbox.petra.examples.tradingsystem2.objects.marketdata.AbstractFeed;
import io.cognitionbox.petra.examples.tradingsystem2.objects.marketdata.Quote;
import io.cognitionbox.petra.examples.tradingsystem2.objects.marketdata.QuoteImp;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.PrecisionNum;

public class LiveFeed extends AbstractFeed {
    private boolean started = false;

    @Override
    public boolean isStarted(String instrument) {
        return started;
    }

    @Override
    public void start(String instrument) {
        this.started = true;
    }

    @Override
    public Quote getQuote(String instrument) {
        Num bid = PrecisionNum.valueOf(Math.random());
        Num ask = PrecisionNum.valueOf(Math.random());
        return new QuoteImp(bid, ask, bid.plus(ask).dividedBy(PrecisionNum.valueOf(2)), bid.minus(ask).abs());
    }

    @Override
    public void addQuote(String instrument, Quote quote) {
        this.getBarSeriesForInstrument(instrument).addPrice(quote.getMid());
    }
}
