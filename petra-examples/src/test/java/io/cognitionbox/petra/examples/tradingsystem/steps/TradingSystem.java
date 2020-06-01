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
import io.cognitionbox.petra.examples.tradingsystem.steps.trade.Trade;
import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.*;

public class TradingSystem extends PGraph<State> {
    {
        setSleepPeriod(1000);
        pi(State.class, x->x.currentExp().get()>=0 && x.currentExp().get()<=200 &&
                forAll(Trader.class,x.traders(),t->t.isEnabled()));
        lc(x->
                x.currentExp().get()==0 ^ x.currentExp().get()==40 ^ x.currentExp().get()==80
                        ^ x.currentExp().get()==120 ^ x.currentExp().get()==160);
        step(new Trade());
        join(state->true,
            state->state.updateExposure(),
            state->true);
        qi(State.class, x->x.isAtMaxExposure());
    }
}