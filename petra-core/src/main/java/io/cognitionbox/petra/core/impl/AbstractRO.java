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

import io.cognitionbox.petra.lang.RO;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractRO<T> implements RO<T> {

    @Override
    public String getId() {
        return id;
    }

    protected final String id;

    protected final AtomicBoolean isRead = new AtomicBoolean(false);

    protected AbstractRO(String id) {
        this.id = id;
    }

    protected AbstractRO() {
        this("");
    }

    public boolean isRead(){
        return isRead.get();
    }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    return get().equals(((AbstractRO)o).get());
  }

  @Override
  public int hashCode() {
    return get().hashCode();
  }
}
