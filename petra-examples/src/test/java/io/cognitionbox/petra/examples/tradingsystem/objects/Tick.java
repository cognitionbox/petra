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