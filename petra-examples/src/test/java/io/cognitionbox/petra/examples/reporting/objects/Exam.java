package io.cognitionbox.petra.examples.reporting.objects;

public class Exam {
    public Double getResult() {
        return result;
    }

    private Double result;

    public Exam mark(Double score){
        this.result = score;
        return this;
    }

    public boolean isMarked(){
        return result!=null;
    }

    public boolean isNotMarked(){
        return !isMarked();
    }
}
