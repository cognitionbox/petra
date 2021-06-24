package io.cognitionbox.petra.examples.reporting.steps;

import io.cognitionbox.petra.examples.reporting.objects.School;
import io.cognitionbox.petra.lang.PGraph;

public class ProcessSchool extends PGraph<School> {
    {
        // we need the pre-invariant set to contain the post-invariant set so we can
        // set safety properties that are always checked

        type(School.class);
        kase(
                p -> p.hasPupils() && (!p.allPupilsHaveAverage() ^ p.allPupilsHaveAverage()),
                p -> p.allPupilsHaveAverage());

        steps(s -> s.getAllPupils(), SitExams.class);
        steps(s -> s.getAllPupils(), ProcessExamResults.class);
        esak();
    }
}
