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
package io.cognitionbox.petra.core.impl;

import io.cognitionbox.petra.core.IJoinMatchesProcessor;
import io.cognitionbox.petra.core.engine.petri.IToken;
import io.cognitionbox.petra.core.Id;
import io.cognitionbox.petra.core.engine.petri.impl.Token;
import io.cognitionbox.petra.lang.Guard;
import io.cognitionbox.petra.util.impl.PList;
import io.cognitionbox.petra.util.function.IPredicate;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JoinMatchesProcessor implements IJoinMatchesProcessor, Serializable {

    private ObjectCopyerViaSerialization copyer = new ObjectCopyerViaSerialization();

    protected Stream<IToken> addFilterToStream(boolean copyInputs, Stream<IToken> stream, IPredicate<IToken> filter) {
        return stream.filter(filter).map(x->{
            if (copyInputs){
                try {
                    Object copy = copyer.copy(x.getValue());
                    return new Token(new Id(x.getUniqueId()),copy);
                } catch (Exception e) {}
            }
            return x;
        });
    }

    @Override
    public List<IToken> getMatchesUsingGuard(Guard guard, boolean copyInputs, Collection<IToken> toUse) {
        return (List<IToken>) addFilterToStream(copyInputs,toUse.parallelStream(), x -> guard.test(x.getValue())).collect(Collectors.toCollection(()->new PList<>()));
    }
}
