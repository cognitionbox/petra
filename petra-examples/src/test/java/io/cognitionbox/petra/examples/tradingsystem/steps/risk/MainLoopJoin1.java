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
package io.cognitionbox.petra.examples.tradingsystem.steps.risk;

import io.cognitionbox.petra.core.IRollback;
import io.cognitionbox.petra.examples.tradingsystem.objects.Decisions;
import io.cognitionbox.petra.examples.tradingsystem.objects.State;
import io.cognitionbox.petra.examples.tradingsystem.steps.trade.DecisionsOk;
import io.cognitionbox.petra.lang.PJoin2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.cognitionbox.petra.util.Petra.*;


public class MainLoopJoin1 extends PJoin2<State, Decisions,State> implements IRollback<State> {
    static final Logger LOG = LoggerFactory.getLogger(MainLoopJoin1.class);
    {
       preA(readWrite(BeforeExposure.class, x->x.lastExp().get()<=x.currentExp().get()));
       preB(readConsume(DecisionsOk.class, x->true));
       func(
                (a, b) -> {
                    State state = a.get(0);
                    state.lastExp().set(state.currentExp().get());
                    b.stream().flatMap(d -> d.stream()).forEach(d->state.addExposure(d.exposure()));
                    LOG.info("time: " + state.getTimeInSeconds());
                    LOG.info("currentExp: " + state.currentExp());
                    return state;
                });
        post(returns(AfterExposure.class, x->x.currentExp().get()>x.lastExp().get()));
    }

    @Override
    public void capture(State state) {

    }

    @Override
    public void rollback(State state) {

    }
}
