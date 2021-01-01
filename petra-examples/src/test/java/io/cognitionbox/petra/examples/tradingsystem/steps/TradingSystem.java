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
package io.cognitionbox.petra.examples.tradingsystem.steps;

import io.cognitionbox.petra.examples.tradingsystem.objects.State;
import io.cognitionbox.petra.examples.tradingsystem.objects.Trader;
import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.*;

public class TradingSystem extends PGraph<State> {
    {
        type(State.class); // required to match State instances at runtime
        iterations(6);
        setSleepPeriod(1000); // sets the time period between iterations, its not actually a sleep now, the time is actually measured
        invariant(state->(((( forAll(Trader.class,state.traders(),trader->!trader.hasFeed() && trader.hasEqZeroDecisions()) ||
                forAll(Trader.class,state.traders(),trader->trader.hasFeed() && trader.hasEqZeroDecisions())) ||
                (forAll(Trader.class,state.traders(),trader->trader.hasGtZeroDecisions()))) ||
                (forAll(Trader.class,state.traders(), trader->trader.hasEqZeroDecisions()) && state.getExposureStore().hasExposures())) ||
                (state.getDecisionStore().hasAvgLimitPrice() && state.getExposureStore().hasAvgExposure()))); // safety invariant holds at beginning and after each iteration

        kase(state->forAll(Trader.class,state.traders(),trader->!trader.hasFeed() && trader.hasEqZeroDecisions()),
                state->forAll(Trader.class,state.traders(),trader->trader.hasFeed() && trader.hasGtZeroDecisions())); // pre-condition
        kase(state->state.exposureEq160() && state.getExposureStore().hasAvgExposure() && state.getDecisionStore().hasAvgLimitPrice(),
                state->state.exposureEq200() && state.getExposureStore().hasAvgExposure() && state.getDecisionStore().hasAvgLimitPrice()); // pre-condition
        stepForall(state->state.getTraders(),new SubscribeToFeed(),seq()); // has to be a sequential step as more than 1 steps in graph operate on same or smaller than type Traders, although it is scheduled sequentially stepForall collection is executed in parallel
        stepForall(state->state.getTraders(),new Trade(),seq()); // has to be a sequential step as more than 1 steps in graph operate on same or smaller than type Traders, although it is scheduled sequentially stepForall collection is executed in parallel
        step(state->state,new CollectData(),seq()); // has to be a sequential step as more than 1 steps in graph operate on same or smaller than type State, see steps above and below
        step(state->state.getDecisionStore(),new AnalyzeDecisions(),par()); // can be a parallel step as no other steps operate on same or smaller than type DecisionsStore
        step(state->state.getExposureStore(),new AnalyzeExposures(),par()); // can be a parallel step as no other steps operate on same or smaller than type ExposureStore
    }
}