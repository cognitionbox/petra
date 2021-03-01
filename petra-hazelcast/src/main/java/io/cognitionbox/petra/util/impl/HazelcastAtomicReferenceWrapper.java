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
import io.cognitionbox.petra.config.PetraHazelcastConfig;
import io.cognitionbox.petra.core.impl.AbstractRef;
import io.cognitionbox.petra.lang.RGraphComputer;

import java.io.IOException;

public class HazelcastAtomicReferenceWrapper<T> extends AbstractRef<T> {

    @Override
    public String toString() {
      return "HazelcastAtomicReferenceWrapper{" +
          "value=" + ref.get() +
          '}';
    }

    private transient IAtomicReference<T> ref;

    private void readObject(java.io.ObjectInputStream stream)
        throws IOException, ClassNotFoundException {
      stream.defaultReadObject();
      this.ref = ((IPetraHazelcastConfig) RGraphComputer.getConfig())
              .getHazelcastClient().getAtomicReference(this.name);
    }

    private String name;

    public HazelcastAtomicReferenceWrapper(T value, String name) {
      this.name = name;
      ref = ((IPetraHazelcastConfig) RGraphComputer.getConfig())
              .getHazelcastClient().getAtomicReference(this.name);
      ref.set(value);
    }

    @Override
    public T get() {
      return ref.get();
    }

    @Override
    public void set(T value) {
      ref.set(value);
    }
  }