package io.cognitionbox.petra.examples.simple.compose;

import io.cognitionbox.petra.examples.simple.common.A;
import io.cognitionbox.petra.examples.simple.common.B;
import io.cognitionbox.petra.examples.simple.common.C;

public interface IAB {
    A a();
    B b();
    void setB(B b);
}
