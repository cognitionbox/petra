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

import io.cognitionbox.petra.core.engine.extractors.CollectionExtractor;
import io.cognitionbox.petra.core.engine.extractors.IterableExtractor;
import io.cognitionbox.petra.core.engine.extractors.MapExtractor;

import java.util.function.Supplier;

public class SequentialRootValueExtractor extends AbstractRootValueExtractor {
    @Override
    Supplier<MapExtractor> mapExtractorSupplier() {
        return ()->new MapExtractorImpl();
    }

    @Override
    Supplier<CollectionExtractor> collectionExtractorSupplier() {
        return ()->new SequentialCollectionExtractor();
    }

    @Override
    Supplier<IterableExtractor> iterableExtractorSupplier() {
        return ()->new IterableExtractorImpl();
    }

    @Override
    Supplier<AbstractValueExtractor> objectGraphExtractorSupplier() {
        return ()->new SequentialObjectGraphExtractor();
    }
}
