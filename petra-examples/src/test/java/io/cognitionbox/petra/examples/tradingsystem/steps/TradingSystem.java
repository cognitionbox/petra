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
        //setSleepPeriod(1000); // sets the time period between iterations, its not actually a sleep now, the time is actually measured
        invariant(state->(state.exposureGtZero() || state.exposureEqZero()) && (state.exposureLt200() || state.exposureEq200())); // safety invariant holds at beginning and after each iteration
        pre(state->forAll(Trader.class,state.traders(),trader->!trader.hasFeed() && (trader.hasGtZeroDecisions() || trader.hasEqZeroDecisions()))); // pre-condition
        stepForall(seq(),state->state.getTraders(),new SubscribeToFeed()); // has to be a sequential step as more than 1 stepForall in graph operate on same or smaller than type Traders, although it is scheduled sequentially stepForall iterator is executed in parallel
        stepForall(seq(),state->state.getTraders(),new Trade()); // has to be a sequential step as more than 1 stepForall in graph operate on same or smaller than type Traders, although it is scheduled sequentially stepForall iterator is executed in parallel
        step(seq(),state->state,new CollectData()); // has to be a sequential step as more than 1 stepForall in graph operate on same or smaller than type State, see stepForall above and below
        step(par(), state->state.getDecisionStore(),new AnalyzeDecisions()); // can be a parallel step as no other stepForall operate on same or smaller than type DecisionsStore
        step(par(),state->state.getExposureStore(),new AnalyzeExposures()); // can be a parallel step as no other stepForall operate on same or smaller than type ExposureStore
        elseStep(x->x, Skip.class);
        post(state->state.exposureEq200() && state.getExposureStore().hasAvgExposure() && state.getDecisionStore().hasAvgLimitPrice()); // post-condition, causes the while loop to break once it is met, hence the graph terminates and returns the result
    }
}