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

import io.cognitionbox.petra.core.engine.petri.impl.Token;
import io.cognitionbox.petra.core.impl.Identifyable;
import io.cognitionbox.petra.lang.Void;
import io.cognitionbox.petra.util.impl.PLock;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.function.Supplier;

public abstract class AbstractPlace<M extends Map<String,IToken>> extends Identifyable implements Place {
    protected abstract M getBackingMap();

    public AbstractPlace(String name) {
        super(name);
    }

    private Lock tokensLock = new PLock();

    private <T> T tryLockThenRunnableFinallyUnlock(Supplier<T> supplier) {
        tokensLock.lock();
        try {
            return supplier.get();
        } finally {
            tokensLock.unlock();
        }
    }

    @Override
    public void addValue(Object value) {
        addToken(new Token(value));
    }

    private void addToken(IToken token){
        tryLockThenRunnableFinallyUnlock(() -> {
            if (token.getValue()==null || token.getValue() instanceof Void)
                return null;
            getBackingMap().put(token.getUniqueId(),token);
            return null; // not used
        });
    }

    @Override
    public boolean removeToken(IToken token) {
        return tryLockThenRunnableFinallyUnlock(() -> {
            if (token.getValue()==null || token.getValue() instanceof Void)
                return false;
            getBackingMap().remove(token.getUniqueId(),token);
            return true;
        });
    }


    @Override
    public Collection<IToken> getTokens() {
        return getBackingMap().values();
    }
}