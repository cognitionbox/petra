package io.cognitionbox.petra.examples.tradingsystem2.objects.traders;

import io.cognitionbox.petra.examples.tradingsystem2.objects.decisions.Decision;
import io.cognitionbox.petra.examples.tradingsystem2.objects.decisions.Direction;
import io.cognitionbox.petra.examples.tradingsystem2.objects.marketdata.Quote;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.PrecisionNum;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractTrader implements Trader {

    private boolean doClosePositionsEOD;
    private Quote currentQuote;
    private int currentIndex;
    private List<Decision> decisions = new CopyOnWriteArrayList<>();
    private String instrument;
    private Direction direction;
    private Num entry;
    private Num exit;
    private Num stop;
    private Num beta;
    private Num closedPnl = PrecisionNum.valueOf(0);
    private List<Quote> quoteDiffs;
    private List<Quote> benchmarkDiffs;
    private Num variance;
    private Num covariance;
    private Num betaNeutralQty;

    public AbstractTrader(String instrument, Direction direction) {
        this.instrument = instrument;
        this.direction = direction;
    }

    @Override
    public boolean doClosePositionsEOD() {
        return doClosePositionsEOD;
    }

    @Override
    public void setClosePositionsEOD(boolean value) {
        this.doClosePositionsEOD = value;
    }

    @Override
    public Num getClosedPnl() {
        return closedPnl;
    }

    @Override
    public void setClosedPnl(Num closedPnl) {
        this.closedPnl = closedPnl;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public Num getEntry() {
        return entry;
    }

    @Override
    public void setEntry(Num entry) {
        this.entry = entry;
    }

    @Override
    public Num getExit() {
        return exit;
    }

    @Override
    public void setExit(Num exit) {
        this.exit = exit;
    }

    @Override
    public Num getStop() {
        return stop;
    }

    @Override
    public void setStop(Num stop) {
        this.stop = stop;
    }

    @Override
    public String getInstrument() {
        return instrument;
    }

    public Quote getCurrentQuote() {
        return this.currentQuote;
    }

    @Override
    public void setCurrentQuote(Quote quote) {
        this.currentQuote = quote;
    }

    @Override
    public List<Decision> getDecisions() {
        return decisions;
    }

    @Override
    public Decision getLastDecision() {
        return decisions.get(decisions.size() - 1);
    }

    @Override
    public boolean hasQuote() {
        return this.currentQuote != null;
    }

    @Override
    public void makeDecision(Decision decision) {
        this.decisions.add(decision);
    }

    @Override
    public void incrementIndex() {
        this.currentIndex++;
    }

    @Override
    public int getCurrentIndex() {
        return currentIndex;
    }

    @Override
    public Num getBeta() {
        return beta;
    }

    @Override
    public void setBeta(Num beta) {
        this.beta = beta;
    }

    @Override
    public List<Quote> getQuoteDiffs() {
        return quoteDiffs;
    }

    @Override
    public void setQuoteDiffs(List<Quote> quoteDiffs) {
        this.quoteDiffs = quoteDiffs;
    }

    @Override
    public List<Quote> getBenchmarkDiffs() {
        return this.benchmarkDiffs;
    }

    @Override
    public void setBenchmarkDiffs(List<Quote> benchmarkDiffs) {
        this.benchmarkDiffs = benchmarkDiffs;
    }

    @Override
    public Num getVariance() {
        return variance;
    }

    @Override
    public void setVariance(Num num) {
        variance = num;
    }

    @Override
    public Num getCovariance() {
        return covariance;
    }

    @Override
    public void setCovariance(Num num) {
        covariance = num;
    }

    @Override
    public Num getBetaNeutralQty() {
        return betaNeutralQty;
    }

    @Override
    public void setBetaNeutralQty(Num num) {
        this.betaNeutralQty = num;
    }
}
