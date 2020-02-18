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

import io.cognitionbox.petra.core.engine.petri.IToken;
import io.cognitionbox.petra.core.engine.extractors.IterableExtractor;
import io.cognitionbox.petra.core.engine.extractors.MapExtractor;
import io.cognitionbox.petra.core.engine.petri.Place;
import io.cognitionbox.petra.core.engine.extractors.CollectionExtractor;
import io.cognitionbox.petra.lang.Ref;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

public abstract class AbstractRootValueExtractor extends AbstractValueExtractor {
    private MapExtractor mapExtractor = mapExtractorSupplier().get();
    private CollectionExtractor collectionExtractor = collectionExtractorSupplier().get();
    private IterableExtractor iterableExtractor = iterableExtractorSupplier().get();
    private AbstractValueExtractor objectGraphExtractor = objectGraphExtractorSupplier().get();

    abstract Supplier<MapExtractor> mapExtractorSupplier();
    abstract Supplier<CollectionExtractor> collectionExtractorSupplier();
    abstract Supplier<IterableExtractor> iterableExtractorSupplier();
    abstract Supplier<AbstractValueExtractor> objectGraphExtractorSupplier();

    @Override
    public void extractToPlace(Object value, Place place) {
        if (value == null) {
            return;
        }
        if (value instanceof Ref) {
            return;
        }
        if (isValueExtractable(value)) {
            if (value instanceof Map) {
                mapExtractor.extractToPlace((Map) value, place);
            } else if (value instanceof Collection) {
                collectionExtractor.extractToPlace((Collection) value, place);
            } else if (value instanceof Iterable) {
                iterableExtractor.extractToPlace((Iterable) value, place);
            } else {
                objectGraphExtractor.extractToPlace(value, place);
            }
        } else {
            place.addValue(value);
        }
    }
}
