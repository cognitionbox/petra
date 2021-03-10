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
package io.cognitionbox.petra.examples.tradingsystem2;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.config.PetraConfig;
import io.cognitionbox.petra.examples.tradingsystem2.objects.State;
import io.cognitionbox.petra.examples.tradingsystem2.objects.StateImp;
import io.cognitionbox.petra.examples.tradingsystem2.objects.decisions.Direction;
import io.cognitionbox.petra.examples.tradingsystem2.objects.marketdata.Mode;
import io.cognitionbox.petra.examples.tradingsystem2.objects.traders.LongShortTrader;
import io.cognitionbox.petra.examples.tradingsystem2.objects.traders.Trader;
import io.cognitionbox.petra.examples.tradingsystem2.steps.TradingSystem;
import io.cognitionbox.petra.factory.PetraParallelComponentsFactory;
import io.cognitionbox.petra.factory.PetraSequentialComponentsFactory;
import io.cognitionbox.petra.lang.PComputer;
import org.junit.Test;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.PrecisionNum;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TradingSystem2Test {

    public final static String LONG_INSTRUMENT = "ABC.USUSD";
    public final static String SHORT_INSTRUMENT = "BA.USUSD";

    @Test
    public void test() {

        PetraConfig config = new PetraConfig();
        config
                .setMode(ExecMode.SEQ)
                .setIsReachabilityChecksEnabled(false)
                .setConstructionGuaranteeChecks(false)
                .setDefensiveCopyAllInputsExceptForEffectedInputs(false)
                .setStrictModeExtraConstructionGuarantee(false)
                .setSequentialModeFactory(new PetraSequentialComponentsFactory())
                .setParallelModeFactory(new PetraParallelComponentsFactory())
                .enableStatesLogging();

        PComputer.setConfig(config);

        PComputer<State> computer = new PComputer();
        List<Trader> traderList = new ArrayList<>();

        traderList.add(new LongShortTrader(SHORT_INSTRUMENT, Direction.SHORT));
        traderList.add(new LongShortTrader(LONG_INSTRUMENT, Direction.LONG));

        State state = new StateImp(Mode.HISTORICAL, traderList);

        State output = computer.eval(new TradingSystem(), state);

        Num pnl = output.getTraders().stream().map(t -> t.getClosedPnl()).reduce(PrecisionNum.valueOf(0), Num::plus);
        assertThat(pnl.doubleValue()).isEqualTo(315.26233332103152113621264922180);
        assertThat(output.getTrader(state.getChosenLongShortPair().getToLong()).getClosedPnl().doubleValue()).isEqualTo(123.23233332103152113621264922180);
        assertThat(output.getTrader(state.getChosenLongShortPair().getToShort()).getClosedPnl().doubleValue()).isEqualTo(192.030);
    }
}
