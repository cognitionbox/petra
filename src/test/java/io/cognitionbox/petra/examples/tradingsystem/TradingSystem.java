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
       getGraphComputer().getConfig()
               .enableStatesLogging()
                        .setConstructionGuaranteeChecks(true)
                        .setStrictModeExtraConstructionGuarantee(true);

        Feeds feeds = new Feeds();
        feeds.add(new RandomFeed(InstrumentId.DAX));
        feeds.add(new RandomFeed(InstrumentId.FTSE));
        State state = new State(feeds);

        state.addTrader(new RandomTrader(TraderId.A, InstrumentId.FTSE));
        state.addTrader(new RandomTrader(TraderId.B, InstrumentId.DAX));
        state.addTrader(new RandomTrader(TraderId.C, InstrumentId.FTSE));
        state.addTrader(new RandomTrader(TraderId.D, InstrumentId.DAX));

        State output = (State) getGraphComputer().computeWithInput(new MainLoop(), state);

        assertThat(output.currentExp().get()).isEqualTo(200);
    }
}
