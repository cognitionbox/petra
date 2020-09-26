package io.cognitionbox.petra.examples.reporting.objects;

import java.util.ArrayList;
import java.util.List;

public class Person {
    private String firstName;
    private String sirName;
    private Integer age;

    public Person(String firstName, String sirName, Integer age) {
        this.firstName = firstName;
        this.sirName = sirName;
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSirName() {
        return sirName;
    }

    public Integer getAge() {
        return age;
    }

//    public boolean firstNameStartsWithA(){
//        return getFirstName().startsWith("A");
//    }
}
