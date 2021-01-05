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
package io.cognitionbox.petra.examples.flashinglights;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.examples.flashinglights.objects.Lights;
import io.cognitionbox.petra.lang.PComputer;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class FlashingLightsMain extends BaseExecutionModesTest {
    public FlashingLightsMain(ExecMode execMode) {
        super(execMode);
    }

   @Test
   public void test() {
       PComputer.getConfig()
               .enableStatesLogging()
                        .setMode(ExecMode.PAR)
                        .setConstructionGuaranteeChecks(false)
                        .setStrictModeExtraConstructionGuarantee(false);

        PComputer<Lights> lc = new PComputer();

       Lights output = lc.eval(new FlashingLights(), new Lights());

        assertThat(output.colour.get()== Lights.Colour.RED);
    }
}
