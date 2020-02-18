/**
 * Copyright 2016-2020 Aran Hakki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cognitionbox.petra.examples.tradingsystem.objects;


import io.cognitionbox.petra.examples.tradingsystem.steps.StateOk;
import io.cognitionbox.petra.examples.tradingsystem.steps.StopAtMaxExposure;
import io.cognitionbox.petra.examples.tradingsystem.steps.risk.AfterExposure;
import io.cognitionbox.petra.examples.tradingsystem.steps.risk.BeforeExposure;
import io.cognitionbox.petra.lang.Ref;
import io.cognitionbox.petra.lang.annotations.Extract;

import java.io.Serializable;
import java.time.LocalDateTime;

import static io.cognitionbox.petra.util.Petra.ref;

@Extract
public class State implements Serializable, StateOk, StopAtMaxExposure, BeforeExposure, AfterExposure {
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
}