package io.cognitionbox.petra.core.engine.extractors;

import io.cognitionbox.petra.core.engine.petri.IToken;

public interface ExtractedStore {
    boolean isExtracted(IToken token);
    void markAsExtracted(IToken token);
}
