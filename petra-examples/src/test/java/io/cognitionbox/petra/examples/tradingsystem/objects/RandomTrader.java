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

import java.time.LocalTime;

public class RandomTrader implements Trader {

    private Decisions decisions = new Decisions();

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Decisions getDecisions() {
        return decisions;
    }

    @Override
    public void addDecision(Decision d) {
        this.decisions.add(d);
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

    @Override
    public boolean hasFeed() {
        return this.feed!=null;
    }

    private boolean decisionsNotNull() {
        return getDecisions()!=null;
    }

    @Override
    public boolean hasGtZeroDecisions() {
        return decisionsNotNull() && getDecisions().size()>0;
    }

    @Override
    public boolean hasEqZeroDecisions() {
        return decisionsNotNull() && getDecisions().size()==0;
    }
}
