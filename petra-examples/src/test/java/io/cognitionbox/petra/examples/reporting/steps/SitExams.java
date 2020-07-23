package io.cognitionbox.petra.examples.reporting.steps;

import io.cognitionbox.petra.examples.reporting.objects.Exam;
import io.cognitionbox.petra.examples.reporting.objects.Pupil;
import io.cognitionbox.petra.lang.PEdge;

public class SitExams extends PEdge<Pupil> {
    {
        type(Pupil.class);
        preC(p->!p.takenExams());
        func(p->{
            p.sitExam(new Exam());
            p.sitExam(new Exam());
            p.sitExam(new Exam());
            return p;
        });
        postC(p->p.takenExams());
    }
}
