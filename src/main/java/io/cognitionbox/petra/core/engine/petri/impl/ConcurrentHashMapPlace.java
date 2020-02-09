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
package io.cognitionbox.petra.core.engine.petri.impl;

import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.core.engine.petri.AbstractPlace;
import io.cognitionbox.petra.core.engine.petri.IToken;
import io.cognitionbox.petra.lang.AbstractStep;
import io.cognitionbox.petra.lang.Guard;
import io.cognitionbox.petra.util.function.IPredicate;
import org.javatuples.Pair;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toList;

public final class ConcurrentHashMapPlace extends AbstractPlace<Map<String, IToken>> {

    private Map<String, IToken> map = new ConcurrentHashMap<>();
    @Override
    protected Map<String, IToken> getBackingMap() {
        return map;
    }

    public ConcurrentHashMapPlace(String name) {
        super(name);
    }

    @Override
    public Collection<IToken> filterTokensByValue(IPredicate<Object> filter) {
        return getBackingMap().values().parallelStream().filter(e->filter.test(e.getValue())).collect(toList());
    }

    @Override
    public Optional<IToken> findAny() {
        return getBackingMap().values().parallelStream().findAny();
    }

    @Override
    public boolean tokensMatchedByUniqueStepPreconditions(List<IStep> steps) {
            IPredicate<IToken> predicate =
                    s -> {
                        int matches = 0;
                        for (Object step : steps) {
                            if (step instanceof AbstractStep){
                                if (((AbstractStep) step).evalP(s.getValue())) {
                                    matches++;
                                    if (matches > 1) {
                                        return true;
                                    }
                                }
                            }
                        }
                        for (Object joinTypes : steps) {
                            if (joinTypes instanceof Pair){
                                boolean joinMatches = true;
                                // iterate input types
                                for (Guard type : ((Pair<List<Guard>, Guard>) joinTypes).getValue0()) {
                                    joinMatches = type.test(s) && joinMatches;
                                }
                                if (joinMatches) {
                                    matches++;
                                    if (matches > 1) {
                                        return true;
                                    }
                                }
                            }
                        }
                        return false;
                    };
            return !getBackingMap().values().parallelStream().anyMatch(predicate);
        }


}
