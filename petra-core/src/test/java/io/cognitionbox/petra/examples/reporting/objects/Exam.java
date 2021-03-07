package io.cognitionbox.petra.examples.reporting.objects;

import io.cognitionbox.petra.lang.Ref;

import java.io.Serializable;

import static io.cognitionbox.petra.util.Petra.ref;

public class Exam implements Serializable {
    public Double getResult() {
        return result.get();
    }

    private Ref<Double> result = ref();

    public Exam mark(Double score) {
        this.result.set(score);
        return this;
    }

    public boolean isMarked() {
        return result.get() != null;
    }
}
