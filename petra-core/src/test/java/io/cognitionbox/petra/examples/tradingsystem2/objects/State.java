package io.cognitionbox.petra.examples.tradingsystem2.objects;

import io.cognitionbox.petra.examples.tradingsystem2.objects.factors.ESGLongShortPair;
import io.cognitionbox.petra.examples.tradingsystem2.objects.marketdata.Mode;
import io.cognitionbox.petra.examples.tradingsystem2.objects.marketdata.historical.HistoricalFeed;
import io.cognitionbox.petra.examples.tradingsystem2.objects.traders.Trader;
import org.ta4j.core.num.Num;

import java.util.List;
import java.util.Set;

public interface State {
    HistoricalFeed getHistoricalFeed();

    void setHistoricalFeed(HistoricalFeed historicalFeed);

    List<Trader> getTraders();

    boolean hasTraders();

    boolean hasFeed();

    Mode getMode();

    Set<String> getInstruments();

    Num getClosedPnl();

    void setClosedPnl(Num pnl);

    ESGLongShortPair getChosenLongShortPair();

    void setSelectedLongShortPair(ESGLongShortPair pair);

    Trader getTrader(String instrument);

    boolean isJmxStarted();

    void setJmxStarted(boolean b);

    Num getBudget();
}
