package io.cognitionbox.petra.examples.reporting.steps;

import io.cognitionbox.petra.examples.reporting.objects.Pupil;
import io.cognitionbox.petra.lang.PGraph;

public class ProcessExamResults extends PGraph<Pupil> {
    {
        type(Pupil.class);
        kase(p -> p.takenExams(), p -> p.hasAverage());

        steps(p -> p.getExams(), MarkExam.class);
        step(p -> p, AverageScores.class);
        esak();
    }
}