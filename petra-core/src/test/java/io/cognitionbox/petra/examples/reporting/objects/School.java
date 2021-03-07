package io.cognitionbox.petra.examples.reporting.objects;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static io.cognitionbox.petra.util.Petra.forAll;
import static io.cognitionbox.petra.util.Petra.thereExists;

public class School implements Serializable {

    public List<YearGroup> getYearGroups() {
        return yearGroups;
    }

    private List<YearGroup> yearGroups;

    public School(List<YearGroup> yearGroups) {
        this.yearGroups = yearGroups;
    }

    public boolean hasPupils() {
        return thereExists(YearGroup.class, getYearGroups(), yearGroup -> {
            return thereExists(SchoolClass.class, yearGroup.getSchoolClasses(), schoolClass -> {
                return !schoolClass.getPupils().isEmpty();
            });
        });
    }

    public boolean allPupilsHaveAverage() {
        return forAll(YearGroup.class, getYearGroups(), yearGroup -> {
            return forAll(SchoolClass.class, yearGroup.getSchoolClasses(), schoolClass -> {
                return forAll(Pupil.class, schoolClass.getPupils(), p -> p.hasAverage());
            });
        });
    }

    public Double getAverageScore() {
        return getYearGroups().stream()
                .flatMap(y -> y.getSchoolClasses().stream())
                .flatMap(y -> y.getPupils().stream())
                .mapToDouble(p -> p.getAverage()).average().getAsDouble();
    }

    private Collection<Pupil> cached = null;

    public Collection<Pupil> getAllPupils() {
        if (cached == null) {
            cached = getYearGroups().stream()
                    .flatMap(y -> y.getSchoolClasses().stream())
                    .flatMap(sc -> sc.getPupils().stream()).collect(Collectors.toList());
        }
        return cached;
    }
}
