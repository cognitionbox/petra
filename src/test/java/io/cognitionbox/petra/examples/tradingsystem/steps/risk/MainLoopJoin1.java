/**
 * Copyright 2016-2020 Aran Hakki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
       preB(readConsume(DecisionsOk.class,x->true));
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
