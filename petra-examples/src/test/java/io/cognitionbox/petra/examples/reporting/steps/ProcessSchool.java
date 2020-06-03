package io.cognitionbox.petra.examples.reporting.steps;

import io.cognitionbox.petra.examples.reporting.objects.School;
import io.cognitionbox.petra.lang.PGraph;

public class ProcessSchool extends PGraph<School> {
    {
        // we need the pre-invariant set to contain the post-invariant set so we can
        // set safety properties that are always checked

        pi(School.class, p->!p.isProcessed() ^ p.isProcessed());
        lc(p->!p.isProcessed());
        step(new ProcessExamResults());
        step(new SitExams());
        qi(School.class,p->p.isProcessed());
    }
}
