package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.util.function.IIterable;

public interface PGraphIterable<T> {
    T iterationValue();
    void setIterationValue(T value);
}
