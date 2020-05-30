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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;

public class RandomTrader implements Trader {

    static final Logger LOG = LoggerFactory.getLogger(RandomTrader.class);


    private TraderId id;
    private InstrumentId instrument;

    public RandomTrader(TraderId id, InstrumentId instrument) {
        this.id = id;
        this.instrument = instrument;
    }


    private Decisions decisions = new Decisions();
    @Override
    public Decisions getDecisions() {
        return decisions;
    }

    @Override
    public void addDecision(Decision d) {
        this.decisions.add(d);
    }

    @Override
    public TraderId id() {
        return this.id;
    }

    @Override
    public InstrumentId getInstrument() {
        return instrument;
    }

    @Override
    public Decisions runStrategy(Tick tick) {
        Decisions decisions = new Decisions();
        if (Math.random() > 0.5) {
            decisions.add(new Decision(tick,
                    LocalTime.now(),
                    Direction.BUY,
                    2.0,
                    1.0,
                    Math.random(),
                    10.0));
        } else {
            decisions.add(new Decision(tick,
                    LocalTime.now(),
                    Direction.SELL,
                    1.0,
                    2.0,
                    Math.random(),
                    10.0));
        }
        //LOG.info(decisions.toString());
        return decisions;
    }

    private Feed feed;
    @Override
    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    @Override
    public Feed getFeed() {
        return this.feed;
    }
}
