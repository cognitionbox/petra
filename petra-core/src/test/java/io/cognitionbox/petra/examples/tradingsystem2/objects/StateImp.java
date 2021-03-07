/**
 * Copyright (C) 2016-2020 Aran Hakki.
 * <p>
 * This file is part of Petra.
 * <p>
 * Petra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Petra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Petra.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.cognitionbox.petra.examples.tradingsystem2.objects;

import io.cognitionbox.petra.examples.tradingsystem2.objects.factors.ESGLongShortPair;
import io.cognitionbox.petra.examples.tradingsystem2.objects.factors.ESGLongShortPairImpl;
import io.cognitionbox.petra.examples.tradingsystem2.objects.marketdata.Mode;
import io.cognitionbox.petra.examples.tradingsystem2.objects.marketdata.historical.HistoricalFeed;
import io.cognitionbox.petra.examples.tradingsystem2.objects.traders.Trader;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.PrecisionNum;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.cognitionbox.petra.examples.tradingsystem2.TradingSystem2Main.LONG_INSTRUMENT;
import static io.cognitionbox.petra.examples.tradingsystem2.TradingSystem2Main.SHORT_INSTRUMENT;

public class StateImp implements State {
    // if in live mode, use live feed, if in historical mode, use historical feed
    private HistoricalFeed historicalFeed;
    private List<Trader> traders;
    private Mode mode;
    private Set<String> instruments = new HashSet<>();
    private Num closedPnl = PrecisionNum.valueOf(0);
    private ESGLongShortPair longShortPair = new ESGLongShortPairImpl();
    private boolean jmxIsStarted = false;

    public StateImp(
            Mode mode,
            List<Trader> traders) {
        this.mode = mode;
        this.traders = traders;
        this.instruments.add("USA500.IDXUSD");
        this.instruments.addAll(traders.stream().map(t -> t.getInstrument()).collect(Collectors.toSet()));
    }

    @Override
    public HistoricalFeed getHistoricalFeed() {
        return historicalFeed;
    }

    @Override
    public void setHistoricalFeed(HistoricalFeed historicalFeed) {
        this.historicalFeed = historicalFeed;
    }

    public List<Trader> getTraders() {
        return traders;
    }

    @Override
    public boolean hasTraders() {
        return traders != null && !traders.isEmpty();
    }

    @Override
    public boolean hasFeed() {
        return historicalFeed != null;
    }

    @Override
    public Mode getMode() {
        return mode;
    }

    @Override
    public Set<String> getInstruments() {
        return instruments;
    }

    @Override
    public Num getClosedPnl() {
        return closedPnl;
    }

    @Override
    public void setClosedPnl(Num pnl) {
        this.closedPnl = pnl;
    }

    @Override
    public ESGLongShortPair getChosenLongShortPair() {
        return longShortPair;
    }

    @Override
    public void setSelectedLongShortPair(ESGLongShortPair pair) {
        this.longShortPair = pair;
    }

    @Override
    public Trader getTrader(String instrument) {
        return getTraders().stream().filter(t -> t.getInstrument().equals(instrument)).findAny().get();
    }

    @Override
    public boolean isJmxStarted() {
        return jmxIsStarted;
    }

    @Override
    public void setJmxStarted(boolean b) {
        this.jmxIsStarted = b;
    }

    @Override
    public Num getBudget() {
        return PrecisionNum.valueOf(100);
    }

    public double getPriceForUSA500() {
        return this.getHistoricalFeed().getQuote("USA500.IDXUSD").getMid().doubleValue();
    }

    public double getPriceForShortInstrument() {
        return this.getHistoricalFeed().getQuote(SHORT_INSTRUMENT).getMid().doubleValue();
    }

    public double getPriceForLongInstrument() {
        return this.getHistoricalFeed().getQuote(LONG_INSTRUMENT).getMid().doubleValue();
    }

    public double getSystemPnl() {
        return this.closedPnl.doubleValue();
    }

    public double getLongPnl() {
        return getTrader(getChosenLongShortPair().getToLong()).getClosedPnl().doubleValue();
    }

    public double getShortPnl() {
        return getTrader(getChosenLongShortPair().getToShort()).getClosedPnl().doubleValue();
    }

}