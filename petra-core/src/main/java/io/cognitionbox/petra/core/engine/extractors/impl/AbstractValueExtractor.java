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
import io.cognitionbox.petra.core.engine.extractors.ValueExtractor;
import io.cognitionbox.petra.core.engine.petri.IToken;
import io.cognitionbox.petra.lang.annotations.Extract;

import java.util.function.Predicate;

public abstract class AbstractValueExtractor<E> implements ValueExtractor<E> {
    public boolean isValueExtractable(IToken<E> value, ExtractedStore extractedStore, Predicate<IToken> extractIfMatches){
        if (value.getValue().getClass().isAnnotationPresent(Extract.class) &&
                                                !extractedStore.isExtracted(value) &&
                                                    extractIfMatches.test(value)) {
            return true;
        }
        return false;
    }
}
