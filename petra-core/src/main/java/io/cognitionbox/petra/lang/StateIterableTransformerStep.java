package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.util.function.IFunction;

public class StateIterableTransformerStep<X, P> {
    public IFunction<X, Iterable<P>> getTransformer() {
        return transformer;
    }
    public AbstractStep getStep() {
        return step;
    }
    private IFunction<X, Iterable<P>> transformer;
    private AbstractStep step;

    public boolean isSeq() {
        return isSeq;
    }

    private boolean isSeq = false;
    public StateIterableTransformerStep(IFunction<X, Iterable<P>> transformer, AbstractStep step, boolean isSeq) {
        this.transformer = transformer;
        this.step = step;
        this.isSeq = isSeq;
    }
}