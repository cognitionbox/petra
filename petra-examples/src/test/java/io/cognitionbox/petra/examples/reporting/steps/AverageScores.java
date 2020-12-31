package io.cognitionbox.petra.examples.reporting.steps;

import io.cognitionbox.petra.examples.reporting.objects.Exam;
import io.cognitionbox.petra.examples.reporting.objects.Pupil;
import io.cognitionbox.petra.lang.PEdge;
import static io.cognitionbox.petra.util.Petra.forAll;

public class AverageScores extends PEdge<Pupil> {
    {
        type(Pupil.class);
        pre(p -> !p.getExams().isEmpty() && !p.hasAverage() && forAll(Exam.class, p.getExams(), e -> e.isMarked()));
        func(p -> {
            p.setAverage(p.getExams().stream().mapToDouble(e -> e.getResult()).average().getAsDouble());
        });
        post(p -> p.hasAverage());
    }
}