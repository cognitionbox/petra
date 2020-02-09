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