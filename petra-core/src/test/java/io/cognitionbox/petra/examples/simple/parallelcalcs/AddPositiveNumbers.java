package io.cognitionbox.petra.examples.simple.parallelcalcs;

import io.cognitionbox.petra.lang.Ref;

import java.io.Serializable;

import static io.cognitionbox.petra.util.Petra.ref;

public class AddPositiveNumbers implements Serializable {
    int a = 4;
    int b = 7;
    Ref<Integer> result = ref(0);
}
