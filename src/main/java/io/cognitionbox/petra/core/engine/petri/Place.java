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
package io.cognitionbox.petra.core.engine.petri;

import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.util.function.IPredicate;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface Place {
    void addValue(Object value);
    boolean removeToken(IToken token);
    Collection<IToken> getTokens();
    Collection<IToken> filterTokensByValue(IPredicate<Object> filter);
    Optional<IToken> findAny();
    boolean tokensMatchedByUniqueStepPreconditions(List<IStep> steps);
}
