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
        setSleepPeriod(1000); // sets the time period between iterations, its not actually a sleep now, the time is actually measured
        invariant(x->(x.exposureGtZero() || x.exposureEqZero()) && (x.exposureLt200() || x.exposureEq200())); // safety invariant holds at beginning and after each iteration
        pre(x->(x.exposureGtZero() || x.exposureEqZero()) && forAll(Trader.class,x.traders(),t->t.isEnabled())); // pre-condition
        stepForall(state->state.getTraders(),new SubscribeToFeed(),seq()); // has to be a sequential step as more than 1 steps in graph operate on same or smaller than type Traders, although it is scheduled sequentially stepForall iterator is executed in parallel
        stepForall(state->state.getTraders(),new Trade(),seq()); // has to be a sequential step as more than 1 steps in graph operate on same or smaller than type Traders, although it is scheduled sequentially stepForall iterator is executed in parallel
        step(state->state,new CollectData(),seq()); // has to be a sequential step as more than 1 steps in graph operate on same or smaller than type State, see steps above and below
        step(state->state.getDecisionStore(),new AnalyzeDecisions(),par()); // can be a parallel step as no other steps operate on same or smaller than type DecisionsStore
        step(state->state.getExposureStore(),new AnalyzeExposures(),par()); // can be a parallel step as no other steps operate on same or smaller than type ExposureStore
        post(x->x.exposureEq200()); // post-condition, causes the while loop to break once it is met, hence the graph terminates and returns the result
    }
}