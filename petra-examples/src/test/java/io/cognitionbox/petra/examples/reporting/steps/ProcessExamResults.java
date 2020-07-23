package io.cognitionbox.petra.examples.reporting.steps;

import io.cognitionbox.petra.examples.reporting.objects.Pupil;
import io.cognitionbox.petra.lang.PGraph;

public class ProcessExamResults extends PGraph<Pupil> {
    {
        type(Pupil.class);
        loopC(p->p.takenExams() && p.hasNoAverage());
        stepForall(p->p.getExams(),new MarkExam());
        step(new AverageScores());
        postC(p->p.hasAverage());
    }
}