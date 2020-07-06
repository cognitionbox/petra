package io.cognitionbox.petra.examples.reporting.steps;

import io.cognitionbox.petra.examples.reporting.objects.Exam;
import io.cognitionbox.petra.examples.reporting.objects.Pupil;
import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.forAll;

public class ProcessExamResults extends PGraph<Pupil> {
    {
        pc(Pupil.class, p->p.takenExams() && p.hasNoAverage() && forAll(Exam.class,p.getExams(), e->e.isNotMarked()));
        step(new AverageScores());
        step(new MarkExam());
        qc(Pupil.class, p->p.hasAverage());
    }
}