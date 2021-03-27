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
package io.cognitionbox.petra.examples.reporting;

import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.examples.reporting.objects.Pupil;
import io.cognitionbox.petra.examples.reporting.objects.School;
import io.cognitionbox.petra.examples.reporting.objects.SchoolClass;
import io.cognitionbox.petra.examples.reporting.objects.Teacher;
import io.cognitionbox.petra.examples.reporting.objects.YearGroup;
import io.cognitionbox.petra.examples.reporting.steps.ProcessSchool;
import io.cognitionbox.petra.lang.PComputer;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import io.cognitionbox.petra.util.impl.PList;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class SchoolExampleTest extends BaseExecutionModesTest {
    public SchoolExampleTest(ExecMode execMode) {
        super(execMode);
    }

    @Test
    public void test() {
        if (!getExecMode().isSEQ()){
            // currently seems to be deadlocking in PAR/DIS mode for this example, needs investigating.
            return;
        }

        PComputer<School> lc = new PComputer();

        Teacher teacher1 = new Teacher("Sam", "Bloggs", 31);

        List<Pupil> pupils = new PList<>();

        pupils.add(new Pupil("Adam", "Johnson", 11));
        pupils.add( new Pupil("Aobby", "Jimbo", 12));

        SchoolClass classA = new SchoolClass(teacher1, pupils);
        List<SchoolClass> classes = new PList<>();
        classes.add(classA);
        YearGroup year7 = new YearGroup(classes);

        List<YearGroup> yearGroups = new PList<>();
        yearGroups.add(year7);

        School school = new School(yearGroups);
        School output = lc.eval(new ProcessSchool(), school);

        assertThat(output.getAverageScore()).isEqualTo(1);
    }
}
