package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.util.function.IFunction;

public class StateTransformerStep<X, P> implements TransformerStep<X> {
    public IFunction<X, P> getTransformer() {
        return transformer;
    }
    public AbstractStep getStep() {
        return step;
    }

    @Override
    public int getSequence() {
        return seq;
    }

    private IFunction<X, P> transformer;
    private AbstractStep step;
    private int seq = 0;
    public StateTransformerStep(int seq, IFunction<X, P> transformer, AbstractStep step) {
        this.transformer = transformer;
        this.step = step;
        this.seq = seq;
    }
}