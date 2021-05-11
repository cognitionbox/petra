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
package io.cognitionbox.petra.examples.analyze;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.config.PetraConfig;
import io.cognitionbox.petra.examples.analyze.objects.ImageData;
import io.cognitionbox.petra.examples.analyze.objects.Pixel;
import io.cognitionbox.petra.examples.analyze.objects.SunlightData;
import io.cognitionbox.petra.examples.analyze.steps.SR1;
import io.cognitionbox.petra.factory.PetraParallelComponentsFactory;
import io.cognitionbox.petra.factory.PetraSequentialComponentsFactory;
import io.cognitionbox.petra.lang.PComputer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SR1Test {

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

        PComputer<SunlightData> computer = new PComputer();
        List<Pixel> pixels = new ArrayList<>();
        pixels.add(new Pixel(2));
        pixels.add(new Pixel(2));
        pixels.add(new Pixel(2));
        pixels.add(new Pixel(2));
        ImageData imageData = new ImageData(pixels);
        SunlightData sunlightData = new SunlightData(imageData);
        SunlightData output = computer.eval(new SR1(), sunlightData);
        assertThat(output.validated==true).isTrue();
    }
}
