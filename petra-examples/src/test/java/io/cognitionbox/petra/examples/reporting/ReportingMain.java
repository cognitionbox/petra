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
package io.cognitionbox.petra.examples.reporting;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.examples.reporting.objects.*;
import io.cognitionbox.petra.examples.reporting.steps.ProcessSchool;
import io.cognitionbox.petra.examples.tradingsystem.objects.*;
import io.cognitionbox.petra.examples.tradingsystem.steps.StateOk;
import io.cognitionbox.petra.examples.tradingsystem.steps.TradingSystem;
import io.cognitionbox.petra.lang.PComputer;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ReportingMain extends BaseExecutionModesTest {
    public ReportingMain(ExecMode execMode) {
        super(execMode);
    }
   @Test
   public void test() {
       PComputer.getConfig()
               .enableStatesLogging()
                        .setConstructionGuaranteeChecks(false)
                        .setStrictModeExtraConstructionGuarantee(true);

       PComputer<School> lc = new PComputer();

       Teacher teacher1 = new Teacher("Sam","Bloggs",31);
       SchoolClass classA = new SchoolClass(teacher1, Arrays.asList(
               new Pupil("Adam","Johnson",11),
               new Pupil("Aobby","Jimbo",12)));
       YearGroup year7 = new YearGroup(Arrays.asList(classA));
       School school = new School(Arrays.asList(year7));
       School output = lc.eval(new ProcessSchool(), school);
       assertThat(output.getAverageScore()).isEqualTo(1);
    }
}
