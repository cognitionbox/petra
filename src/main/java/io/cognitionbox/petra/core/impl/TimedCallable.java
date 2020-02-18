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

import io.cognitionbox.petra.util.function.ICallable;
import com.google.common.primitives.Doubles;

import java.io.Serializable;

public class TimedCallable<T> implements Comparable<TimedCallable<T>> ,
        Serializable {

    private final ICallable<T> callable;
    private long totTime = 0;
    private long obs = 0;

    public TimedCallable(ICallable callable) {
      this.callable = callable;
    }

    private void addTimeObservation(long time) {
      totTime += time;
      obs++;
    }

    public T executeAndTime() {
      long now = System.currentTimeMillis();
      T value = null;
      try {
        value = callable.call();
      } catch (Exception e) {
        e.printStackTrace();
      }
      long end = System.currentTimeMillis();
      addTimeObservation(end - now);
      return value;
    }

    private double getAvgTime() {
      return obs == 0 ? 0 : totTime / obs;
    }

    @Override
    public int compareTo(TimedCallable o) {
      return Doubles.compare(getAvgTime(), o.getAvgTime());
    }


    @Override
    public String toString() {
      return "TimedCallable [avgTime=" + getAvgTime() + "]";
    }
  }