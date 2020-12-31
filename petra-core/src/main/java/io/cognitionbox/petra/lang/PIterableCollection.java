package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.util.function.IIterable;

public interface PIterableCollection<T> {
    T iterationValue();
    void setIterationValue(T value);
}
