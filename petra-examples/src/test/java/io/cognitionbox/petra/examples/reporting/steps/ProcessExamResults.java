package io.cognitionbox.petra.examples.reporting.steps;

import io.cognitionbox.petra.examples.reporting.objects.Pupil;
import io.cognitionbox.petra.lang.PGraph;

public class ProcessExamResults extends PGraph<Pupil> {
    {
        type(Pupil.class);
        kase(p->p.takenExams(),p->p.hasAverage());
        kase(p->!p.takenExams(),p->!p.hasAverage());
        stepForall(p->p.getExams(),new MarkExam());
        step(p->p,new AverageScores());
    }
}