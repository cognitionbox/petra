package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.core.IStep;

public interface TransformerStep<X> extends Comparable<TransformerStep>{
    IStep<X> getStep();
    int getSequence();
    default int compareTo(TransformerStep o){
        return Integer.compare(this.getSequence(),o.getSequence());
    }
}
