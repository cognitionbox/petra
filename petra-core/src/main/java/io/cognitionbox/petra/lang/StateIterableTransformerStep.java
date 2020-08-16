package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.util.function.IFunction;

public class StateIterableTransformerStep<X, P> implements TransformerStep {
    public IFunction<X, Iterable<P>> getTransformer() {
        return transformer;
    }
    public AbstractStep getStep() {
        return step;
    }
    private IFunction<X, Iterable<P>> transformer;
    private AbstractStep step;

    public StateIterableTransformerStep(IFunction<X, Iterable<P>> transformer, AbstractStep step) {
        this.transformer = transformer;
        this.step = step;
    }
}