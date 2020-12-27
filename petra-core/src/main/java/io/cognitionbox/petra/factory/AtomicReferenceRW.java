package io.cognitionbox.petra.factory;

import io.cognitionbox.petra.lang.RW;

import java.util.concurrent.atomic.AtomicBoolean;

public class AtomicReferenceRW<T> extends AtomicReferenceRO<T> implements RW<T> {

      protected final AtomicBoolean isWritten = new AtomicBoolean(false);

      public boolean isWritten(){
          return isWritten.get();
      }

      public AtomicReferenceRW(T value, String id) {
          super(value,id);
      }

      @Override
      public void set(T value) {
          atomicReference.set(value);
          this.isWritten.set(true);
      }
  }