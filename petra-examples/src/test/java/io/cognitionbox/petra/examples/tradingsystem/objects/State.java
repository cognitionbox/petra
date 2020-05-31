/**
 * Copyright (C) 2016-2020 Aran Hakki.
 *
 * This file is part of Petra.
 *
 * Petra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Petra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Petra.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.cognitionbox.petra.examples.tradingsystem.objects;


import io.cognitionbox.petra.examples.tradingsystem.steps.TradingSystem;
import io.cognitionbox.petra.examples.tradingsystem.steps.StateOk;
import io.cognitionbox.petra.examples.tradingsystem.steps.MaxExposure;
import io.cognitionbox.petra.examples.tradingsystem.steps.risk.AfterExposure;
import io.cognitionbox.petra.examples.tradingsystem.steps.risk.BeforeExposure;
import io.cognitionbox.petra.examples.tradingsystem.steps.trade.GetTraders;
import io.cognitionbox.petra.lang.Ref;
import io.cognitionbox.petra.lang.annotations.Extract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;

import static io.cognitionbox.petra.util.Petra.ref;

@Extract
public class State implements Serializable, GetTraders, StateOk, MaxExposure, BeforeExposure, AfterExposure {
    private Ref<Double> lastExp = ref();
    private Ref<Double> currentExp = ref();

    public boolean state(){
        return true;
    }

    private Traders traders = new Traders();
    @Extract
    public Traders traders(){
        return traders;
    }
    private Feeds feeds;

    public State(Feeds feeds) {
        this.feeds = feeds;
        if (!hasAllFeeds()) {
            throw new IllegalStateException();
        }
        lastExp.set(0d);
        currentExp.set(0d);
    }

    public void addTrader(Trader trader) {
        trader.setFeed(getFeedForInstrument(trader.getInstrument()));
        traders.add(trader);
    }

    public Traders getTraders() {
        return traders;
    }

    public Ref<Double> currentExp() {
        return currentExp;
    }

    @Override
    public Ref<Double> lastExp() {
        return lastExp;
    }

    synchronized public void addExposure(Double exp) {
        currentExp.set(currentExp.get()+exp);
    }

    public int getTimeInSeconds() {
        return LocalDateTime.now().getSecond();
    }

    public Feed getFeedForInstrument(InstrumentId instrument) {
        return feeds.stream().filter(f -> f.getInstrument().equals(instrument)).findFirst().orElse(null);
    }

    public boolean hasFeed(InstrumentId instrument) {
        return getFeedForInstrument(instrument) != null;
    }

    public boolean hasAllFeeds() {
        for (Trader t : traders) {
            if (!hasFeed(t.getInstrument())) {
                return false;
            }
        }
        return true;
    }

    public void updateExposure(){
        lastExp().set(currentExp().get());
        currentExp().set(
                traders().stream()
                        .flatMap(d -> d.getDecisions().stream()).mapToDouble(d->d.exposure()).sum());
    }
}