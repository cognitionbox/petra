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
import io.cognitionbox.petra.examples.tradingsystem.steps.marketdata.SourceTraderTick;
import io.cognitionbox.petra.examples.tradingsystem.steps.risk.MainLoopJoin1;
import io.cognitionbox.petra.examples.tradingsystem.steps.trade.Trade;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.util.Petra;

import static io.cognitionbox.petra.util.Petra.ro;
import static io.cognitionbox.petra.util.Petra.rt;

public class MainLoop extends PGraph<State, State> {
    {
        setMaxIterations(1);
        setSleepPeriod(1000);
        pre(ro(StateOk.class, x->true));
        step(new SourceTraderTick());
        step(new Trade());
        joinSome(new MainLoopJoin1());
        post(Petra.rt(StopAtMaxExposure.class, x->x.currentExp().get()>=200));
    }
}