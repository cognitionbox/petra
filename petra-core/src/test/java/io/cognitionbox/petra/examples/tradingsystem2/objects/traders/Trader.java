package io.cognitionbox.petra.examples.tradingsystem2.objects.traders;

import io.cognitionbox.petra.examples.tradingsystem2.objects.decisions.Decision;
import io.cognitionbox.petra.examples.tradingsystem2.objects.decisions.Direction;
import io.cognitionbox.petra.examples.tradingsystem2.objects.marketdata.Quote;
import org.ta4j.core.num.Num;

import java.util.List;

public interface Trader {
    List<Decision> getDecisions();

    Decision getLastDecision();

    boolean hasQuote();

    void makeDecision(Decision decision);

    void incrementIndex();

    int getCurrentIndex();

    Direction getDirection();

    String getInstrument();

    Num getBeta();

    void setBeta(Num num);

    Num getVariance();

    void setVariance(Num num);

    Num getCovariance();

    void setCovariance(Num num);

    Num getEntry();

    void setEntry(Num num);

    Num getExit();

    void setExit(Num num);

    Num getStop();

    void setStop(Num num);

    Num getClosedPnl();

    void setClosedPnl(Num pnl);

    boolean doClosePositionsEOD();

    void setClosePositionsEOD(boolean value);

    Num getBetaNeutralQty();

    void setBetaNeutralQty(Num num);

    Quote getCurrentQuote();

    void setCurrentQuote(Quote quote);

    List<Quote> getQuoteDiffs();

    void setQuoteDiffs(List<Quote> quoteDiffs);

    List<Quote> getBenchmarkDiffs();

    void setBenchmarkDiffs(List<Quote> quoteDiffs);
}
