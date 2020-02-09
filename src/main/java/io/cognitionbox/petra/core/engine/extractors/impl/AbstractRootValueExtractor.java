/**
 * Copyright (C) 2016-2020 Aran Hakki.
 *
 * This file is part of Petra.
 *
 * Petra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Petra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Petra.  If not, see <https://www.gnu.org/licenses/>.
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
