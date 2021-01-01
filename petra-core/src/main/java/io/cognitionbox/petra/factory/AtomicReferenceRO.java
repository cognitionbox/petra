package io.cognitionbox.petra.factory;

import io.cognitionbox.petra.core.impl.AbstractRO;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceRO<T> extends AbstractRO<T> {

        protected AtomicReference<T> atomicReference;

        @Override
        public String toString() {
            return "AtomicReferenceImpl{" +
                    "ref=" + atomicReference +
                    '}';
        }

        public AtomicReferenceRO(T value, String stepName, String id){
            super(stepName,id);
            atomicReference = new AtomicReference(value);
        }

        public AtomicReferenceRO(T value, String stepName){
            this(value,stepName,"");
        }

        @Override
        public T get() {
            T value = atomicReference.get();
            this.isRead.set(true);
            return value;
        }

    }