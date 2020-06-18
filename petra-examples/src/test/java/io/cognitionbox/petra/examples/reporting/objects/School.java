package io.cognitionbox.petra.examples.reporting.objects;

import io.cognitionbox.petra.lang.annotations.Extract;

import java.util.ArrayList;
import java.util.List;

import static io.cognitionbox.petra.util.Petra.*;

@Extract
public class School {
    @Extract public List<YearGroup> getYearGroups() {
        return yearGroups;
    }

    private List<YearGroup> yearGroups;

    public School(List<YearGroup> yearGroups) {
        this.yearGroups = yearGroups;
    }

    public boolean hasPupils(){
        return thereExists(YearGroup.class,getYearGroups(),yearGroup -> {
            return thereExists(SchoolClass.class,yearGroup.getSchoolClasses(),schoolClass -> {
                return !schoolClass.getPupils().isEmpty();
            });
        });
    }

    public boolean allPupilsHaveAverage(){
        return forAll(YearGroup.class,getYearGroups(),yearGroup -> {
            return forAll(SchoolClass.class,yearGroup.getSchoolClasses(),schoolClass -> {
                return forAll(Pupil.class,schoolClass.getPupils(),p->p.hasAverage());
            });
        });
    }

    public boolean notAllPupilsHaveAverage(){
        return !allPupilsHaveAverage();
    }

    public Double getAverageScore(){
        return getYearGroups().stream()
                .flatMap(y->y.getSchoolClasses().stream())
                .flatMap(y->y.getPupils().stream())
                .mapToDouble(p->p.getAverage()).average().getAsDouble();
    }
}
