package io.cognitionbox.petra.examples.reporting.steps;

import io.cognitionbox.petra.examples.reporting.objects.Exam;
import io.cognitionbox.petra.examples.reporting.objects.Pupil;
import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.forAll;

public class ProcessExamResults extends PGraph<Pupil> {
    {
        pc(Pupil.class, p->p.takenExams() && p.hasNoAverage());
        step(p->p.getExams(),new MarkExam());
        step(new AverageScores());
        qc(Pupil.class, p->p.hasAverage());
    }
}