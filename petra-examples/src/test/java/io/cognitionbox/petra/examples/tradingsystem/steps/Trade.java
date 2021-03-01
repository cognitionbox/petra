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
package io.cognitionbox.petra.examples.tradingsystem.steps;


import io.cognitionbox.petra.examples.tradingsystem.objects.Decisions;
import io.cognitionbox.petra.examples.tradingsystem.objects.Trader;
import io.cognitionbox.petra.lang.PEdge;


public class Trade extends PEdge<Trader> {
    {
        type(Trader.class);
        pre(trader -> trader.hasFeed() && (trader.hasGtZeroDecisions() || trader.hasEqZeroDecisions()));
        func(
                trader -> {
                    Decisions decisions = trader.runStrategy(trader.getFeed().sourceTick());
                    decisions.forEach(decision -> trader.addDecision(decision));
                }
        );
        post(trader -> trader.hasGtZeroDecisions());
    }
}