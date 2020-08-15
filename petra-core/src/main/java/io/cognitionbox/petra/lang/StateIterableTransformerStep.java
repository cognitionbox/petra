package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.util.function.IFunction;

public class StateIterableTransformerStep<X, P> implements TransformerStep<X> {
    public IFunction<X, Iterable<P>> getTransformer() {
        return transformer;
    }
    public AbstractStep getStep() {
        return step;
    }

    @Override
    public int getSequence() {
        return seq;
    }

    private IFunction<X, Iterable<P>> transformer;
    private AbstractStep step;
    private int seq = 0;
    public StateIterableTransformerStep(int seq, IFunction<X, Iterable<P>> transformer, AbstractStep step) {
        this.transformer = transformer;
        this.step = step;
        this.seq = seq;
    }
}