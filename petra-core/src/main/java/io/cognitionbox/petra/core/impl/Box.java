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

import java.io.Serializable;

public class Box<T> implements Serializable {
  private final T boxed;

  @Override
  public String toString() {
    return this.getClass().getSimpleName()+"{" +
        "boxed=" + boxed +
        '}';
  }

  public Box(T boxed) {
    this.boxed = boxed;
  }
  public T unbox(){
    return boxed;
  }

  @Override
  final public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Box<?> box = (Box<?>) o;

    return boxed != null ? boxed.equals(box.boxed) : box.boxed == null;
  }

  @Override
  final public int hashCode() {
    return boxed != null ? boxed.hashCode() : 0;
  }
}
