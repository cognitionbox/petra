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
import io.cognitionbox.petra.examples.tradingsystem.steps.trade.UpdateExp;
import io.cognitionbox.petra.lang.PGraph;
import org.junit.Ignore;

import static io.cognitionbox.petra.util.Petra.*;

@Ignore
public class TradingSystem extends PGraph<State> {
    {
        type(State.class);
        setSleepPeriod(1000);
        pre(x->x.currentExp().get()>=0 && x.currentExp().get()<=160 &&
                forAll(Trader.class,x.traders(),t->t.isEnabled()));
        stepForall(x->x.getTraders(),new Trade());
        step(new UpdateExp());
        post(x->x.isAtMaxExposure());
    }
}