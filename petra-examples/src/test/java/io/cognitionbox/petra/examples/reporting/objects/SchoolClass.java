package io.cognitionbox.petra.examples.reporting.objects;

import io.cognitionbox.petra.lang.annotations.Extract;

import java.io.Serializable;
import java.util.List;

@Extract
public class SchoolClass implements Serializable {
    private Teacher teacher;
    private List<Pupil> pupils;

    public Teacher getTeacher() {
        return teacher;
    }

    @Extract
    public List<Pupil> getPupils() {
        return pupils;
    }

    public SchoolClass(Teacher teacher, List<Pupil> pupils) {
        this.teacher = teacher;
        this.pupils = pupils;
    }
}
