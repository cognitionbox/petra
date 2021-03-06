package io.cognitionbox.petra.examples.reporting.objects;

import java.io.Serializable;

public class Teacher extends Person implements Serializable {
    public Teacher(String firstName, String sirName, Integer age) {
        super(firstName, sirName, age);
    }
}
