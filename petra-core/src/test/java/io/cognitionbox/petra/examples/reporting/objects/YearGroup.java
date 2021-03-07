package io.cognitionbox.petra.examples.reporting.objects;

import io.cognitionbox.petra.lang.annotations.Extract;

import java.io.Serializable;
import java.util.List;

@Extract
public class YearGroup implements Serializable {
    private List<SchoolClass> schoolClasses;

    public YearGroup(List<SchoolClass> schoolClasses) {
        this.schoolClasses = schoolClasses;
    }

    @Extract
    public List<SchoolClass> getSchoolClasses() {
        return schoolClasses;
    }
}
