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

import io.cognitionbox.petra.core.impl.Identifyable;
import io.cognitionbox.petra.core.engine.petri.IToken;
import io.cognitionbox.petra.core.Id;

public class Token<E> extends Identifyable implements IToken<E>{
    private E value;

    public E getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Token{" +
                "value=" + value +
                '}';
    }

    public Token(Id id, E value){
        super(id);
        this.value = value;
    }

    public Token(E value){
        this.value = value;
    }
}
