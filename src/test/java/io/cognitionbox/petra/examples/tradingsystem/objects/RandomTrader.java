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
