package io.cognitionbox.petra.examples.reporting.steps;

import io.cognitionbox.petra.examples.reporting.objects.Exam;
import io.cognitionbox.petra.examples.reporting.objects.Pupil;
import io.cognitionbox.petra.lang.PEdge;

import static io.cognitionbox.petra.util.Petra.forAll;
import static io.cognitionbox.petra.util.Petra.thereExists;

public class ScoreAveraged extends PEdge<Pupil> {
    {
        pc(Pupil.class, p -> (!p.hasAverage() ^ p.hasAverage()) && p.firstNameStartsWithA() && forAll(Exam.class, p.getExams(), e -> e.isMarked()));
        func(p -> {
            p.setAverage(p.getExams().stream().mapToDouble(e -> e.getResult()).average().getAsDouble());
            return p;
        });
        qc(Pupil.class, p -> p.hasAverage() && p.firstNameStartsWithA() && forAll(Exam.class, p.getExams(), e -> e.isMarked()));
    }
}