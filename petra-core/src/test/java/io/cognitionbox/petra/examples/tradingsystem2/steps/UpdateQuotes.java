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
package io.cognitionbox.petra.examples.tradingsystem2.steps;


import io.cognitionbox.petra.examples.tradingsystem2.objects.State;
import io.cognitionbox.petra.examples.tradingsystem2.objects.marketdata.Quote;
import io.cognitionbox.petra.examples.tradingsystem2.objects.marketdata.historical.HistoricalFeed;
import io.cognitionbox.petra.examples.tradingsystem2.objects.traders.Trader;
import io.cognitionbox.petra.lang.PCollectionEdge;

import static io.cognitionbox.petra.util.Petra.seq;


public class UpdateQuotes extends PCollectionEdge<State, HistoricalFeed, Trader> {
    {
        type(State.class);
        collection(state -> state.getTraders());
        shared(seq(),state -> state.getHistoricalFeed());
        pre((feed,trader) -> feed.isStarted(trader.getInstrument()) && feed.getQuote(trader.getInstrument())!=null);
        func(
                (feed, trader) -> {
                    Quote quote = feed.getQuote(trader.getInstrument());
                    feed.step();
                    trader.setCurrentQuote(quote);
                    trader.getDecisions().forEach(d -> {
                        d.setCurrentQuote(quote);
                    });
                }
        );
        post((feed, trader) -> true);
    }
}