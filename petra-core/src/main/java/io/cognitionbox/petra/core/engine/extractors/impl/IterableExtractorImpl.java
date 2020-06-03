/**
 * Copyright 2016-2020 Aran Hakki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cognitionbox.petra.core.engine.extractors.impl;

import io.cognitionbox.petra.core.engine.extractors.ExtractedStore;
import io.cognitionbox.petra.core.engine.petri.IToken;
import io.cognitionbox.petra.core.engine.extractors.IterableExtractor;
import io.cognitionbox.petra.core.engine.petri.Place;
import io.cognitionbox.petra.core.engine.petri.impl.Token;

import java.util.function.Predicate;

public class IterableExtractorImpl extends AbstractValueExtractor<Iterable> implements IterableExtractor {

    @Override
    public void extractToPlace(IToken<Iterable> iterable, Place place, ExtractedStore extractedStore, Predicate<IToken> extractIfMatches) {
        for (Object e : iterable.getValue()){
            new SequentialRootValueExtractor().extractToPlace(new Token(e),place,extractedStore,extractIfMatches);
            //place.addValue(e);
        }
    }
}
