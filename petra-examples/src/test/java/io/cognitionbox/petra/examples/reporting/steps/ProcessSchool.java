package io.cognitionbox.petra.examples.reporting.steps;

import io.cognitionbox.petra.examples.reporting.objects.School;
import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.seq;

public class ProcessSchool extends PGraph<School> {
    {
        // we need the pre-invariant set to contain the post-invariant set so we can
        // set safety properties that are always checked

        type(School.class);
        pre(p->p.hasPupils() && (!p.allPupilsHaveAverage() ^ p.allPupilsHaveAverage()));
        post(p->p.allPupilsHaveAverage());
        stepForall(s->s.getAllPupils(),
                new SitExams(),seq());
        stepForall(s->s.getAllPupils(),
                new ProcessExamResults(),seq());
    }
}
