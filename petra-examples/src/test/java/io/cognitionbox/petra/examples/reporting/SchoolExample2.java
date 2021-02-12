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
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static io.cognitionbox.petra.util.Petra.forAll;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class SchoolExample2 extends BaseExecutionModesTest {
    public SchoolExample2(ExecMode execMode) {
        super(execMode);
    }
   @Test
   public void test() {

       Teacher teacher1 = new Teacher("Sam","Bloggs",31);
       SchoolClass classA = new SchoolClass(teacher1, Arrays.asList(
               new Pupil("Adam","Johnson",11),
               new Pupil("Aobby","Jimbo",12)));
       YearGroup year7 = new YearGroup(Arrays.asList(classA));
       School school = new School(Arrays.asList(year7));


       School output = processSchool(school);

       assertThat(output.getAverageScore()).isEqualTo(1);

    }

    public School processSchool(School school){
        if (school.hasPupils() && (!school.allPupilsHaveAverage() ^ school.allPupilsHaveAverage())){
            while (!school.allPupilsHaveAverage()){
                for (Pupil p : school.getAllPupils()){
                    processExamResults(p);
                }
                for (Pupil p : school.getAllPupils()){
                    sitExams(p);
                }
                if (!(school.hasPupils() && (!school.allPupilsHaveAverage() ^ school.allPupilsHaveAverage()))){
                    throw new IllegalStateException("invariant breach.");
                }
            }
        }
        return school;
    }

    private void sitExams(Pupil p) {
        if (!p.takenExams()){
            p.sitExam(new Exam());
            p.sitExam(new Exam());
            p.sitExam(new Exam());
            if (p.takenExams()){
                return;
            } else {
                throw new IllegalStateException("post-condition failure.");
            }
        }
    }

    private void processExamResults(Pupil p) {
        if (p.takenExams()){
            while (!p.hasAverage()){
                for (Exam exam : p.getExams()){
                    markExam(exam);
                }
                averageScores(p);
                if (!p.takenExams()){
                    throw new IllegalStateException("invariant breach.");
                }
            }
        }
    }

    private void averageScores(Pupil p) {
        if (!p.hasAverage() && forAll(Exam.class, p.getExams(), e -> e.isMarked())){
            p.setAverage(p.getExams().stream().mapToDouble(e -> e.getResult()).average().getAsDouble());
            if (p.hasAverage()){
                return;
            } else {
                throw new IllegalStateException("post-condition failure.");
            }
        }
    }

    private void markExam(Exam exam) {
        if (!exam.isMarked()){
            exam.mark(1d);
            if (exam.isMarked()){
                return;
            } else {
                throw new IllegalStateException("post-condition failure.");
            }
        }
    }
}
