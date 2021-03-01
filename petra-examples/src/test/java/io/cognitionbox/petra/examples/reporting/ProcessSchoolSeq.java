package io.cognitionbox.petra.examples.reporting;

import io.cognitionbox.petra.examples.reporting.objects.Exam;
import io.cognitionbox.petra.examples.reporting.objects.Pupil;
import io.cognitionbox.petra.examples.reporting.objects.School;
import io.cognitionbox.petra.examples.reporting.objects.SchoolClass;
import io.cognitionbox.petra.examples.reporting.objects.Teacher;
import io.cognitionbox.petra.examples.reporting.objects.YearGroup;
import org.junit.Test;

import java.util.Arrays;

import static io.cognitionbox.petra.util.Petra.forAll;
import static org.assertj.core.api.Assertions.assertThat;

public class ProcessSchoolSeq {

    @Test
    public void test() {

        Teacher teacher1 = new Teacher("Sam", "Bloggs", 31);
        SchoolClass classA = new SchoolClass(teacher1, Arrays.asList(
                new Pupil("Adam", "Johnson", 11),
                new Pupil("Aobby", "Jimbo", 12)));
        YearGroup year7 = new YearGroup(Arrays.asList(classA));
        School school = new School(Arrays.asList(year7));


        School output = processSchool(school);

        assertThat(output.getAverageScore()).isEqualTo(1);

    }

    public School processSchool(School school) {
        if (school.hasPupils() && (!school.allPupilsHaveAverage() ^ school.allPupilsHaveAverage()))
            while (!(school.allPupilsHaveAverage())) {
                for (Pupil p : school.getAllPupils()) {
                    processExamResults(p);
                }
                for (Pupil p : school.getAllPupils()) {
                    sitExams(p);
                }
                if (!(school.hasPupils() && (!school.allPupilsHaveAverage() ^ school.allPupilsHaveAverage()))) {
                    throw new IllegalStateException();
                } else {
                }
            }
        else {
        }
        return school;
    }

    public Pupil processExamResults(Pupil pupil) {
        if (pupil.takenExams())
            while (!(pupil.hasAverage())) {
                for (Exam e : pupil.getExams()) {
                    markExam(e);
                }
                averageScores(pupil);
                if (!(pupil.takenExams())) {
                    throw new IllegalStateException();
                } else {
                }
            }
        else {
        }
        return pupil;
    }

    public Exam markExam(Exam exam) {
        if (!exam.isMarked()) {
            {
                exam.mark(1d);
            }
            if (exam.isMarked()) {
            } else {
                throw new IllegalStateException();
            }
        } else {
        }
        return exam;
    }

    public Pupil averageScores(Pupil pupil) {
        if (!pupil.hasAverage() && forAll(Exam.class, pupil.getExams(), e -> e.isMarked())) {
            {
                pupil.setAverage(pupil.getExams().stream().mapToDouble(e -> e.getResult()).average().getAsDouble());
            }
            if (pupil.hasAverage()) {
            } else {
                throw new IllegalStateException();
            }
        } else {
        }
        return pupil;
    }

    public Pupil sitExams(Pupil pupil) {
        if (!pupil.takenExams()) {
            {
                pupil.sitExam(new Exam());
                pupil.sitExam(new Exam());
                pupil.sitExam(new Exam());
            }
            if (pupil.takenExams()) {
            } else {
                throw new IllegalStateException();
            }
        } else {
        }
        return pupil;
    }
}
