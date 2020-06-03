package io.cognitionbox.petra.examples.reporting.objects;

import java.util.List;

public class Class {
    private Teacher teacher;
    private List<Pupil> pupils;

    public Teacher getTeacher() {
        return teacher;
    }

    public List<Pupil> getPupils() {
        return pupils;
    }

    public Class(Teacher teacher, List<Pupil> pupils) {
        this.teacher = teacher;
        this.pupils = pupils;
    }
}
