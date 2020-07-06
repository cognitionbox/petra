package io.cognitionbox.petra.examples.reporting.steps;

import io.cognitionbox.petra.examples.reporting.objects.School;
import io.cognitionbox.petra.lang.PGraph;

import java.util.stream.Collectors;

public class ProcessSchool extends PGraph<School> {
    {
        // we need the pre-invariant set to contain the post-invariant set so we can
        // set safety properties that are always checked

        gi(p->p.hasPupils() && (p.allPupilsHaveAverage() ^ !p.allPupilsHaveAverage()));
        pc(School.class, p->p.hasPupils() && !p.allPupilsHaveAverage());
        step(s->s.getAllPupils(),
                new ProcessExamResults());
        step(s->s.getAllPupils(),
                new SitExams());
        qc(School.class, p->p.allPupilsHaveAverage());
    }
}
