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


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Tick implements Serializable {
    private final static AtomicInteger counter = new AtomicInteger();
    LocalDateTime now = LocalDateTime.now();
    int id;
    InstrumentId instrument;
    Double bid;
    Double ask;
    TraderId destinationTraderId;

    public Tick(InstrumentId instrument, Double bid, Double ask) {
        this.id = counter.getAndIncrement();
        this.instrument = instrument;
        this.bid = bid;
        this.ask = ask;
    }

    public Double getBid() {

        return bid;
    }

    public Double getAsk() {
        return ask;
    }

    public InstrumentId getInstrument() {
        return instrument;
    }

    public TraderId getDestination() {
        return this.destinationTraderId;
    }

    public Tick setDestination(TraderId destinationTraderId) {
        this.destinationTraderId = destinationTraderId;
        return this;
    }
}