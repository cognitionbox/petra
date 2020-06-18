package io.cognitionbox.petra.examples.reporting.steps;

import io.cognitionbox.petra.examples.reporting.objects.Exam;
import io.cognitionbox.petra.examples.reporting.objects.Pupil;
import io.cognitionbox.petra.lang.PEdge;

import static io.cognitionbox.petra.util.Petra.forAll;

public class SitExams extends PEdge<Pupil> {
    {
        pc(Pupil.class,p->p.takenNoExams());
        func(p->{
            p.sitExam(new Exam());
            p.sitExam(new Exam());
            p.sitExam(new Exam());
            return p;
        });
        qc(Pupil.class,p->p.takenExams());
    }
}
