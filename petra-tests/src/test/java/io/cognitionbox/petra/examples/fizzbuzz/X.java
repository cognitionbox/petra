package io.cognitionbox.petra.examples.fizzbuzz;

import java.util.ArrayList;
import java.util.List;

public class X {

    private final List<String> lines = new ArrayList<>();

    public void addLine(String line) {
        System.out.println("Adding line: " + line);
        lines.add(line);
    }

    int lineCount() {
        return lines.size();
    }

    boolean isEmpty() {
        return lines.isEmpty();
    }

    public int i = 1;

    public boolean isFizzBuzz() {
        final List<String> expected = new ArrayList<>();

        for (int i = 1; i <= 15; i++) {
            if (i % 3 == 0 && i % 5 == 0) {
                expected.add("Fizz Buzz");
            } else if (i % 3 == 0) {
                expected.add("Fizz");
            } else {
                expected.add("Buzz");
            }
        }

        return lines.equals(expected);
    }
}