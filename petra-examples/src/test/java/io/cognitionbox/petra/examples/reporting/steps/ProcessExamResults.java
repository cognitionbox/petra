package io.cognitionbox.petra.examples.reporting.steps;

import io.cognitionbox.petra.examples.reporting.objects.Pupil;
import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.seq;

public class ProcessExamResults extends PGraph<Pupil> {
    {
        type(Pupil.class);
        iterations(2);
        kase(p->p.takenExams(),p->p.hasAverage());
        kase(p->!p.takenExams(),p->!p.hasAverage());
        stepForall(p->p.getExams(),new MarkExam(),seq());
        step(p->p,new AverageScores(),seq());
    }
}