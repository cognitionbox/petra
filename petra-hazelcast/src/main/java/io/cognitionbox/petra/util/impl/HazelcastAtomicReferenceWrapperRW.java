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
package io.cognitionbox.petra.util.impl;

import com.hazelcast.core.IAtomicReference;
import io.cognitionbox.petra.config.IPetraHazelcastConfig;
import io.cognitionbox.petra.core.impl.AbstractRO;
import io.cognitionbox.petra.lang.RGraphComputer;
import io.cognitionbox.petra.lang.RW;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class HazelcastAtomicReferenceWrapperRW<T> extends HazelcastAtomicReferenceWrapperRO<T> implements RW<T> {

    protected final AtomicBoolean isWritten = new AtomicBoolean(false);

    public boolean isWritten(){
        return isWritten.get();
    }

    public HazelcastAtomicReferenceWrapperRW(T value, String name) {
        super(value, name);
    }

    @Override
    public void set(T value) {
        atomicReference.set(value);
        this.isWritten.set(true);
    }
  }