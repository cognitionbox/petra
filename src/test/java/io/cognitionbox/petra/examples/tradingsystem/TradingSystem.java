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
package io.cognitionbox.petra.examples.tradingsystem;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.config.PetraConfig;
import io.cognitionbox.petra.examples.simple.common.B;
import io.cognitionbox.petra.examples.tradingsystem.objects.*;
import io.cognitionbox.petra.examples.tradingsystem.steps.MainLoop;
import io.cognitionbox.petra.factory.PetraParallelComponentsFactory;
import io.cognitionbox.petra.lang.PGraphComputer;
import io.cognitionbox.petra.lang.RGraphComputer;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.assertj.core.api.Assertions.assertThat;
@RunWith(Parameterized.class)
public class TradingSystem extends BaseExecutionModesTest {
    public TradingSystem(ExecMode execMode) {
        super(execMode);
    }

    /*
     * Here we have a scalable algorithmic trading system which
     * supports multiple parallel trading strategies.
     *
     * For this example we have 4 random traders, randomly buying or selling.
     * Each trader is bound to a data feed, FTSE or DAX.
     *
     * The system works by polling the feeds for ticks every 5 seconds and
     * produces a tick for each trading strategy. Each tick has a destination field
     * and an instrument field so that the tick its consumed by the correct trader.
     *
     */
   @Test
   public void test() {
       RGraphComputer.getConfig()
               .enableStatesLogging()
                        .setConstructionGuaranteeChecks(true)
                        .setStrictModeExtraConstructionGuarantee(true);

        PGraphComputer<State,State> lc = new PGraphComputer();

        Feeds feeds = new Feeds();
        feeds.add(new RandomFeed(InstrumentId.DAX));
        feeds.add(new RandomFeed(InstrumentId.FTSE));
        State state = new State(feeds);

        state.addTrader(new RandomTrader(TraderId.A, InstrumentId.FTSE));
        state.addTrader(new RandomTrader(TraderId.B, InstrumentId.DAX));
        state.addTrader(new RandomTrader(TraderId.C, InstrumentId.FTSE));
        state.addTrader(new RandomTrader(TraderId.D, InstrumentId.DAX));

        State output = lc.computeWithInput(new MainLoop(), state);

        assertThat(output.currentExp().get()).isEqualTo(200);
    }
}
