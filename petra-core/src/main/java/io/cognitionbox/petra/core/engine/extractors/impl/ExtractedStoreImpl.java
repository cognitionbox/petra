package io.cognitionbox.petra.core.engine.extractors.impl;

import io.cognitionbox.petra.core.engine.extractors.ExtractedStore;
import io.cognitionbox.petra.core.engine.petri.IToken;
import io.cognitionbox.petra.core.impl.Identifyable;
import io.cognitionbox.petra.util.impl.PMap;

import java.io.Serializable;
import java.util.Map;

public class ExtractedStoreImpl extends Identifyable implements ExtractedStore {
    private Map<String,Boolean> extracted = new PMap<>();
    @Override
    public boolean isExtracted(IToken token) {
        return extracted.containsKey(token.getUniqueId());
    }

    @Override
    public void markAsExtracted(IToken token) {
        extracted.put(token.getUniqueId(),true);
    }
}
