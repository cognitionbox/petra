package io.cognitionbox.petra.lang;


import java.util.Collection;

public interface PIterateCollection<T> {
    Collection<T> collection();
    T iterationValue();
    void setIterationValue(T value);
}
