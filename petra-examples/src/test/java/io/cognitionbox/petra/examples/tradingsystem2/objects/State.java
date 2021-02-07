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
package io.cognitionbox.petra.examples.tradingsystem2.objects;

import io.cognitionbox.petra.examples.tradingsystem2.objects.Trader;

import java.util.List;

public class State {
    // if in live mode, use live feed, if in historical mode, use historical feed
    private Feed feed;
    private List<Trader> traders;
    Feed getFeed(){
        return feed;
    }

    public void addTrader(Trader trader){
        traders.add(trader);
    }
}