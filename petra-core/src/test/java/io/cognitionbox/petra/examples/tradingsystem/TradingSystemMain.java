/**
 * Copyright (C) 2016-2020 Aran Hakki.
 * <p>
 * This file is part of Petra.
 * <p>
 * Petra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Petra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Petra.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.cognitionbox.petra.examples.tradingsystem;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.examples.tradingsystem.objects.RandomTrader;
import io.cognitionbox.petra.examples.tradingsystem.objects.State;
import io.cognitionbox.petra.examples.tradingsystem.steps.TradingSystem;
import io.cognitionbox.petra.lang.PComputer;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
@RunWith(Parameterized.class)
public class TradingSystemMain extends BaseExecutionModesTest {
    public TradingSystemMain(ExecMode execMode) {
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
        PComputer.getConfig()
                .enableStatesLogging()
                .setMode(ExecMode.PAR)
                .setConstructionGuaranteeChecks(false)
                .setStrictModeExtraConstructionGuarantee(false);

        PComputer<State> lc = new PComputer();
        State state = new State();

        state.addTrader(new RandomTrader());
        state.addTrader(new RandomTrader());
        state.addTrader(new RandomTrader());
        state.addTrader(new RandomTrader());

        State output = lc.eval(new TradingSystem(), state);

        assertThat(output.getExposureStore().getExposure()).isEqualTo(200);
        assertThat(output.getExposureStore().hasAvgExposure()).isEqualTo(true);
        assertThat(output.getDecisionStore().hasAvgLimitPrice()).isEqualTo(true);
    }
}
