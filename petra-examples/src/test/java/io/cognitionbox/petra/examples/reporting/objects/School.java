package io.cognitionbox.petra.examples.reporting.objects;

import io.cognitionbox.petra.lang.annotations.Extract;

import java.util.ArrayList;
import java.util.List;

import static io.cognitionbox.petra.util.Petra.forAll;
import static io.cognitionbox.petra.util.Petra.ref;

@Extract
public class School {
    @Extract public List<YearGroup> getYearGroups() {
        return yearGroups;
    }

    private List<YearGroup> yearGroups;

    public School(List<YearGroup> yearGroups) {
        this.yearGroups = yearGroups;
    }

    public boolean isProcessed(){
        return forAll(YearGroup.class,getYearGroups(),yearGroup -> {
            return forAll(SchoolClass.class,yearGroup.getSchoolClasses(),schoolClass -> {
                return forAll(Pupil.class,schoolClass.getPupils(),p->p.hasAverage());
            });
        });
    }

    public Double getAverageScore(){
        return getYearGroups().stream()
                .flatMap(y->y.getSchoolClasses().stream())
                .flatMap(y->y.getPupils().stream())
                .mapToDouble(p->p.getAverage()).average().getAsDouble();
    }
}
