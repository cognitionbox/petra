package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.util.function.IFunction;

public class StateTransformerStep<X, P> {
    public IFunction<X, P> getTransformer() {
        return transformer;
    }
    public AbstractStep getStep() {
        return step;
    }
    private IFunction<X, P> transformer;
    private AbstractStep step;

    public boolean isSeq() {
        return isSeq;
    }

    private boolean isSeq = false;
    public StateTransformerStep(IFunction<X, P> transformer, AbstractStep step, boolean isSeq) {
        this.transformer = transformer;
        this.step = step;
        this.isSeq = isSeq;
    }
}