package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.core.IStep;

import java.io.Serializable;

public interface TransformerStep extends Serializable {
    IStep getStep();
}
