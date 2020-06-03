package io.cognitionbox.petra.examples.reporting.objects;

import io.cognitionbox.petra.lang.annotations.Extract;

import java.util.ArrayList;
import java.util.List;

@Extract
public class Pupil extends Person {
    public Pupil(String firstName, String sirName, Integer age) {
        super(firstName, sirName, age);
    }
    private List<Exam> exams = new ArrayList<>();
    private Double average;

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public boolean hasAverage(){
        return this.average!=null;
    }

    public void sitExam(Exam exam){
        exams.add(exam);
    }
    @Extract public List<Exam> getExams() {
        return exams;
    }

    @Override
    public String toString() {
        return "Pupil{" +
                "exams=" + exams +
                ", average=" + average +
                '}';
    }
}
