package io.cognitionbox.petra.factory;

import io.cognitionbox.petra.lang.RW;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceRW<T> extends AtomicReferenceRO<T> implements RW<T> {

        protected final AtomicBoolean isWritten = new AtomicBoolean(false);

        public boolean isWritten(){
          return isWritten.get();
      }

        public AtomicReferenceRW(T value, String stepName, String id){
            super(value,stepName,id);
        }

        public AtomicReferenceRW(T value, String stepName){
            this(value,stepName,"");
        }

      @Override
      public void set(T value) {
          atomicReference.set(value);
          this.isWritten.set(true);
      }
  }