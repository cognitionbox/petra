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

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class HazelcastAtomicReferenceWrapperRO<T> extends AbstractRO<T> {

    protected final AtomicBoolean isRead = new AtomicBoolean(false);

    public boolean isRead(){
        return isRead.get();
    }

    @Override
    public String toString() {
      return "HazelcastAtomicReferenceWrapper{" +
          "value=" + atomicReference.get() +
          '}';
    }

    protected transient IAtomicReference<T> atomicReference;

    private void readObject(java.io.ObjectInputStream stream)
        throws IOException, ClassNotFoundException {
      stream.defaultReadObject();
      this.atomicReference = ((IPetraHazelcastConfig) RGraphComputer.getConfig())
              .getHazelcastClient().getAtomicReference(this.id);
    }

    public HazelcastAtomicReferenceWrapperRO(T value, String id) {
        super(id);
        atomicReference = ((IPetraHazelcastConfig) RGraphComputer.getConfig())
              .getHazelcastClient().getAtomicReference(this.id);
        atomicReference.set(value);
    }

    @Override
    public T get() {
        T value = atomicReference.get();
        this.isRead.set(true);
        return value;
    }

  }