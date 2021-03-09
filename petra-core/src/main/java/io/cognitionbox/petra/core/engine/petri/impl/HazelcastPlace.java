/**
 * Copyright 2016-2020 Aran Hakki
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cognitionbox.petra.core.engine.petri.impl;

import com.hazelcast.core.IMap;
import io.cognitionbox.petra.core.engine.petri.AbstractPlace;
import io.cognitionbox.petra.core.engine.petri.IToken;
import io.cognitionbox.petra.lang.RGraphComputer;
import io.cognitionbox.petra.lang.config.IPetraHazelcastConfig;

import java.io.IOException;
import java.util.Optional;

public final class HazelcastPlace extends AbstractPlace<IMap<String, IToken>> {

    transient private IMap<String, IToken> map = ((IPetraHazelcastConfig) RGraphComputer.getConfig())
            .getHazelcastClient()
            .getMap(getUniqueId());

    @Override
    protected IMap<String, IToken> getBackingMap() {
        return map;
    }

    public HazelcastPlace(String name) {
        super(name);
    }

    @Override
    public Optional<IToken> findAny() {
        return getBackingMap().values().stream().findAny();
    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        map = ((IPetraHazelcastConfig) RGraphComputer.getConfig())
                .getHazelcastClient()
                .getMap(getUniqueId());
    }

}
