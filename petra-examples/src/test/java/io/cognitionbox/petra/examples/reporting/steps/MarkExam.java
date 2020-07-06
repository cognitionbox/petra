package io.cognitionbox.petra.examples.reporting.steps;

import io.cognitionbox.petra.examples.reporting.objects.Exam;
import io.cognitionbox.petra.lang.PEdge;

public class MarkExam extends PEdge<Exam> {
    {
        type(Exam.class);
        pc(e->e.isNotMarked());
        func(e->e.mark(1d));
        qc(e->e.isMarked());
    }
}
