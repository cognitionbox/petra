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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IToken){
            return this.getUniqueId().equals(((IToken) obj).getUniqueId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getUniqueId().hashCode();
    }
}
