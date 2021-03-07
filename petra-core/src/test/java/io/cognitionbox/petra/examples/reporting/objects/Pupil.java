package io.cognitionbox.petra.examples.reporting.objects;

import io.cognitionbox.petra.lang.Ref;
import io.cognitionbox.petra.lang.annotations.Extract;
import io.cognitionbox.petra.util.impl.PList;

import java.io.Serializable;
import java.util.List;

import static io.cognitionbox.petra.util.Petra.ref;

//@Extract
public class Pupil extends Person implements Serializable {
    public Pupil(String firstName, String sirName, Integer age) {
        super(firstName, sirName, age);
    }

    private List<Exam> exams = new PList<>();
    private Ref<Double> average = ref();

    public Double getAverage() {
        return average.get();
    }

    public void setAverage(Double average) {
        this.average.set(average);
    }

    public boolean hasAverage() {
        return this.average.get() != null;
    }

    public void sitExam(Exam exam) {
        exams.add(exam);
    }

    @Extract
    public List<Exam> getExams() {
        return exams;
    }

    @Override
    public String toString() {
        return "Pupil{" +
                "exams=" + exams +
                ", average=" + average +
                '}';
    }

    public boolean takenExams() {
        return !getExams().isEmpty();
    }
}
