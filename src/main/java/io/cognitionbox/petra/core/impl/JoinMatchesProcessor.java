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
