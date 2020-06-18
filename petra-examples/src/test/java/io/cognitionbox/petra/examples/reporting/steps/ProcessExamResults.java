package io.cognitionbox.petra.examples.reporting.steps;

import io.cognitionbox.petra.examples.reporting.objects.Exam;
import io.cognitionbox.petra.examples.reporting.objects.Pupil;
import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.forAll;

public class ProcessExamResults extends PGraph<Pupil> {
    {
        pi(Pupil.class,p->p.takenExams());
        lc(p->p.hasNoAverage() && forAll(Exam.class,p.getExams(),e->e.isNotMarked()));
        step(new AverageScores());
        step(new MarkExam());
        qi(Pupil.class,p->p.hasAverage());
    }
}