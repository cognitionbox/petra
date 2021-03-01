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
package io.cognitionbox.petra.core.engine.petri.impl;

import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.core.engine.petri.AbstractPlace;
import io.cognitionbox.petra.core.engine.petri.IToken;
import io.cognitionbox.petra.lang.AbstractStep;
import io.cognitionbox.petra.lang.Guard;
import io.cognitionbox.petra.util.function.IPredicate;
import org.javatuples.Pair;

import java.util.*;

import static java.util.stream.Collectors.toList;

public final class HashMapPlace extends AbstractPlace<Map<String, IToken>> {

    private Map<String, IToken> map = new HashMap<>();
    @Override
    protected Map<String, IToken> getBackingMap() {
        return map;
    }

    public HashMapPlace(String name) {
        super(name);
    }

    @Override
    public Collection<IToken> filterTokensByValue(IPredicate<Object> filter) {
        return getBackingMap().values().stream().filter(e->filter.test(e.getValue())).collect(toList());
    }

    @Override
    public Optional<IToken> findAny() {
        return getBackingMap().values().stream().findAny();
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
            return !getBackingMap().values().stream().anyMatch(predicate);
        }
}
