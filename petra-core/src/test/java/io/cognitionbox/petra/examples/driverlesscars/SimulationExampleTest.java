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
package io.cognitionbox.petra.examples.driverlesscars;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.lang.PComputer;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class SimulationExampleTest extends BaseExecutionModesTest {
    public SimulationExampleTest(ExecMode execMode) {
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
                .setConstructionGuaranteeChecks(false)
                .setStrictModeExtraConstructionGuarantee(true);

        PComputer<Simlulation> lc = new PComputer();

        Simlulation simlulation = new Simlulation();
        simlulation.loadCars(10);
        Simlulation output = lc.eval(new Simulate(), simlulation);
    }
}
