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

    public void reset(){
        getBackingMap().clear();
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