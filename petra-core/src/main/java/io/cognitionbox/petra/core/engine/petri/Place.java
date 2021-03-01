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
    void reset();
}
